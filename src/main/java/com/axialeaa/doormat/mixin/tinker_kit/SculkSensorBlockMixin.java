package com.axialeaa.doormat.mixin.tinker_kit;

import com.axialeaa.doormat.tinker_kit.TinkerKitUtils;
import com.llamalad7.mixinextras.injector.v2.WrapWithCondition;
import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import com.llamalad7.mixinextras.sugar.Local;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.SculkSensorBlock;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.random.Random;
import net.minecraft.world.World;
import net.minecraft.world.tick.TickPriority;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyArg;

@Mixin(value = SculkSensorBlock.class, priority = 1500)
public abstract class SculkSensorBlockMixin {

    @Shadow protected abstract void scheduledTick(BlockState state, ServerWorld world, BlockPos pos, Random random);

    @ModifyArg(method = "scheduledTick", at = @At(value = "INVOKE", target = "Lnet/minecraft/server/world/ServerWorld;setBlockState(Lnet/minecraft/util/math/BlockPos;Lnet/minecraft/block/BlockState;I)Z"))
    private int modifyUpdateType_scheduledTick(int original, @Local(argsOnly = true) BlockState state) {
        Block block = state.getBlock();
        return TinkerKitUtils.getFlags(block, original);
    }

    @ModifyArg(method = "setCooldown", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/World;setBlockState(Lnet/minecraft/util/math/BlockPos;Lnet/minecraft/block/BlockState;I)Z"))
    private static int modifyUpdateType_setCooldown(int original, @Local(argsOnly = true) BlockState state) {
        Block block = state.getBlock();
        return TinkerKitUtils.getFlags(block, original);
    }

    @ModifyArg(method = "onBlockAdded", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/World;setBlockState(Lnet/minecraft/util/math/BlockPos;Lnet/minecraft/block/BlockState;I)Z"))
    private int modifyUpdateType_onBlockAdded(int original, @Local(argsOnly = true, ordinal = 0) BlockState state) {
        Block block = state.getBlock();
        return TinkerKitUtils.getFlagsWithoutNeighborUpdate(block, original) | Block.FORCE_STATE;
    }

    @ModifyArg(method = "setActive", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/World;setBlockState(Lnet/minecraft/util/math/BlockPos;Lnet/minecraft/block/BlockState;I)Z"))
    private int modifyUpdateType_setActive(int original, @Local(argsOnly = true) BlockState state) {
        Block block = state.getBlock();
        return TinkerKitUtils.getFlags(block, original);
    }

    @WrapWithCondition(method = "updateNeighbors", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/World;updateNeighborsAlways(Lnet/minecraft/util/math/BlockPos;Lnet/minecraft/block/Block;)V"))
    private static boolean shouldUpdateNeighbours(World instance, BlockPos pos, Block sourceBlock) {
        return TinkerKitUtils.shouldUpdateNeighbours(sourceBlock);
    }

    @WrapOperation(method = "setActive", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/World;scheduleBlockTick(Lnet/minecraft/util/math/BlockPos;Lnet/minecraft/block/Block;I)V"))
    private void modifyDelay(World instance, BlockPos pos, Block block, int i, Operation<Void> original) {
        int delay = TinkerKitUtils.getDelay(block, i);

        if (delay > 0) {
            TickPriority tickPriority = TinkerKitUtils.getTickPriority(block);
            instance.scheduleBlockTick(pos, block, delay, tickPriority);
        }
        else if (instance instanceof ServerWorld serverWorld) {
            BlockState blockState = serverWorld.getBlockState(pos);
            this.scheduledTick(blockState, serverWorld, pos, serverWorld.getRandom());
        }
    }

}
