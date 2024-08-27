package com.axialeaa.doormat.mixin.tinker_kit;

import com.axialeaa.doormat.tinker_kit.TinkerKit;
import com.llamalad7.mixinextras.injector.v2.WrapWithCondition;
import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import com.llamalad7.mixinextras.sugar.Local;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.DetectorRailBlock;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.random.Random;
import net.minecraft.world.World;
import net.minecraft.world.tick.TickPriority;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyArg;

@Mixin(value = DetectorRailBlock.class, priority = 1500)
public abstract class DetectorRailBlockMixin {

    @Shadow protected abstract void scheduledTick(BlockState state, ServerWorld world, BlockPos pos, Random random);

    @ModifyArg(method = "updatePoweredStatus", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/World;setBlockState(Lnet/minecraft/util/math/BlockPos;Lnet/minecraft/block/BlockState;I)Z"))
    private int modifyUpdateType(int original, @Local(ordinal = 1) BlockState blockState) {
        Block block = blockState.getBlock();
        return TinkerKit.getFlags(block, original);
    }

    @WrapWithCondition(method = "updateNearbyRails", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/World;updateNeighbor(Lnet/minecraft/block/BlockState;Lnet/minecraft/util/math/BlockPos;Lnet/minecraft/block/Block;Lnet/minecraft/util/math/BlockPos;Z)V"))
    private boolean shouldUpdateNeighbours(World instance, BlockState state, BlockPos pos, Block sourceBlock, BlockPos sourcePos, boolean notify) {
        return TinkerKit.shouldUpdateNeighbours(sourceBlock);
    }

    @WrapWithCondition(method = "updatePoweredStatus", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/World;updateNeighborsAlways(Lnet/minecraft/util/math/BlockPos;Lnet/minecraft/block/Block;)V"))
    private boolean shouldUpdateNeighbours(World instance, BlockPos pos, Block sourceBlock) {
        return TinkerKit.shouldUpdateNeighbours(sourceBlock);
    }

    @WrapOperation(method = "updatePoweredStatus", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/World;scheduleBlockTick(Lnet/minecraft/util/math/BlockPos;Lnet/minecraft/block/Block;I)V"))
    private void modifyDelay(World instance, BlockPos pos, Block block, int i, Operation<Void> original) {
        int delay = TinkerKit.getDelay(block, i);

        if (delay > 0) {
            TickPriority tickPriority = TinkerKit.getTickPriority(block);
            instance.scheduleBlockTick(pos, block, delay, tickPriority);
        }
        else if (instance instanceof ServerWorld serverWorld) {
            BlockState blockState = serverWorld.getBlockState(pos);
            this.scheduledTick(blockState, serverWorld, pos, serverWorld.getRandom());
        }
    }

}
