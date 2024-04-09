package com.axialeaa.doormat.helper;

import com.axialeaa.doormat.DoormatSettings;
import com.axialeaa.doormat.mixin.rule.fireAspectLighting.TntBlockAccessor;
import com.axialeaa.doormat.tinker_kit.TinkerKit;
import net.minecraft.block.*;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemUsageContext;
import net.minecraft.state.property.Properties;
import net.minecraft.util.ActionResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.event.GameEvent;

public class FireAspectLightingHelper {

    /**
     * Ignites TNT, campfires or candles when clicked with the item.
     */
    public static ActionResult onUse(ItemUsageContext ctx) {
        if (DoormatSettings.fireAspectLighting) {
            PlayerEntity player = ctx.getPlayer();
            ItemStack stack = ctx.getStack();
            World world = ctx.getWorld();
            BlockPos blockPos = ctx.getBlockPos();
            BlockState blockState = world.getBlockState(blockPos);

            if (EnchantmentHelper.getFireAspect(player) > 0) {
                if (blockState.getBlock() instanceof TntBlock) {
                    TntBlockAccessor.invokePrimeTnt(world, blockPos, player);
                    world.setBlockState(blockPos, Blocks.AIR.getDefaultState(), TinkerKit.getUpdateFlags(blockState, Block.NOTIFY_ALL) | Block.REDRAW_ON_MAIN_THREAD);
                }
                else if (CampfireBlock.canBeLit(blockState) || CandleBlock.canBeLit(blockState) || CandleCakeBlock.canBeLit(blockState)) {
                    world.setBlockState(blockPos, blockState.with(Properties.LIT, true), Block.NOTIFY_ALL_AND_REDRAW);
                    world.emitGameEvent(player, GameEvent.BLOCK_CHANGE, blockPos);
                }
                else return ActionResult.PASS;

                if (stack.isDamageable() && player != null)
                    stack.damage(1, player, LivingEntity.getSlotForHand(ctx.getHand()));

                return ActionResult.success(world.isClient());
            }
        }
        return ActionResult.PASS;
    }

}
