package com.axialeaa.doormat.helper;

import com.axialeaa.doormat.DoormatSettings;
import com.axialeaa.doormat.mixin.rule.fireAspectLighting.TntBlockAccessor;
import com.axialeaa.doormat.tinker_kit.TinkerKit;
import net.minecraft.block.*;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.enchantment.Enchantments;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemUsageContext;
import net.minecraft.registry.RegistryEntryLookup;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.registry.entry.RegistryEntry;
import net.minecraft.state.property.Properties;
import net.minecraft.util.ActionResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.event.GameEvent;

import java.util.Optional;

public class FireAspectLightingHelper {

    /**
     * Ignites TNT, campfires or candles when clicked with the item.
     */
    public static ActionResult onUse(ItemUsageContext ctx) {
        if (!DoormatSettings.fireAspectLighting)
            return ActionResult.PASS;

        ItemStack stack = ctx.getStack();
        World world = ctx.getWorld();

        if (!hasFireAspect(world, stack))
            return ActionResult.PASS;

        BlockPos blockPos = ctx.getBlockPos();
        BlockState blockState = world.getBlockState(blockPos);

        PlayerEntity player = ctx.getPlayer();
        Block block = blockState.getBlock();

        if (block instanceof TntBlock) {
            TntBlockAccessor.invokePrimeTnt(world, blockPos, player);

            int flags = TinkerKit.getFlags(block, Block.NOTIFY_ALL) | Block.REDRAW_ON_MAIN_THREAD;
            world.setBlockState(blockPos, Blocks.AIR.getDefaultState(), flags);
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

    private static boolean hasFireAspect(World world, ItemStack stack) {
        RegistryEntryLookup<Enchantment> lookup = world.getRegistryManager().createRegistryLookup().getOrThrow(RegistryKeys.ENCHANTMENT);
        Optional<RegistryEntry.Reference<Enchantment>> optional = lookup.getOptional(Enchantments.FIRE_ASPECT);

        return optional.filter(ref -> stack.getEnchantments().getLevel(ref) > 0).isPresent();
    }

}
