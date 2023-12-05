package com.axialeaa.doormat.helpers;

import com.axialeaa.doormat.util.UpdateTypeRules;
import net.minecraft.block.*;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.enchantment.Enchantments;
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
    public static ActionResult light(ItemUsageContext ctx) {
        PlayerEntity player = ctx.getPlayer();
        ItemStack stack = ctx.getStack();
        World world = ctx.getWorld();
        BlockPos pos = ctx.getBlockPos();
        BlockState state = world.getBlockState(pos);

        if (EnchantmentHelper.getLevel(Enchantments.FIRE_ASPECT, stack) > 0) {
            if (state.getBlock() instanceof TntBlock) {
                TntBlock.primeTnt(world, pos);
                world.setBlockState(pos, Blocks.AIR.getDefaultState(), UpdateTypeRules.ruleValues.get(UpdateTypeRules.TNT).getFlags() | Block.REDRAW_ON_MAIN_THREAD);
            }
            else if (CampfireBlock.canBeLit(state) || CandleBlock.canBeLit(state) || CandleCakeBlock.canBeLit(state)) {
                world.setBlockState(pos, state.with(Properties.LIT, true), Block.NOTIFY_ALL | Block.REDRAW_ON_MAIN_THREAD);
                world.emitGameEvent(player, GameEvent.BLOCK_CHANGE, pos);
            }
            else return ActionResult.PASS;
            if (stack.isDamageable() && player != null)
                stack.damage(1, (LivingEntity)player, (p) -> p.sendToolBreakStatus(ctx.getHand()));
            return ActionResult.success(world.isClient());
        }
        return null;
    }

}
