package com.axialeaa.doormat.mixin.rule.observerHalfDelay;

import com.axialeaa.doormat.DoormatSettings;
import com.llamalad7.mixinextras.sugar.Local;
import net.minecraft.block.Block;
import net.minecraft.block.Blocks;
import net.minecraft.block.ObserverBlock;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.WorldAccess;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyArg;

@Mixin(ObserverBlock.class)
public class ObserverBlockMixin {

    @Unique
    private static int halveDelayOnOre(WorldAccess world, BlockPos pos, int delay) {
        return DoormatSettings.observerHalfDelay && world.getBlockState(pos.down()).isOf(Blocks.REDSTONE_ORE) ? Math.max(1, delay / 2) : delay;
    }

    /**
     * @return 1 if the rule is enabled and the block underneath is redstone ore, otherwise the normal delay of 2.
     */
    @ModifyArg(method = "scheduledTick", at = @At(value = "INVOKE", target = "Lnet/minecraft/server/world/ServerWorld;scheduleBlockTick(Lnet/minecraft/util/math/BlockPos;Lnet/minecraft/block/Block;I)V"))
    private int modifyDelay_Scheduled(BlockPos pos, Block block, int delay, @Local(argsOnly = true) ServerWorld world) {
        return halveDelayOnOre(world, pos, delay);
    }

    @ModifyArg(method = "scheduleTick", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/WorldAccess;scheduleBlockTick(Lnet/minecraft/util/math/BlockPos;Lnet/minecraft/block/Block;I)V"))
    private int modifyDelay_Schedule(BlockPos pos, Block block, int delay, @Local(argsOnly = true) WorldAccess world) {
        return halveDelayOnOre(world, pos, delay);
    }

}
