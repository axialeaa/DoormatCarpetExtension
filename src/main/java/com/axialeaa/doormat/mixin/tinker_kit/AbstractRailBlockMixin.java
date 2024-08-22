package com.axialeaa.doormat.mixin.tinker_kit;

import com.axialeaa.doormat.mixin.extensibility.AbstractBlockMixin;
import com.axialeaa.doormat.tinker_kit.TinkerKit;
import com.llamalad7.mixinextras.injector.v2.WrapWithCondition;
import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import com.llamalad7.mixinextras.sugar.Local;
import net.minecraft.block.AbstractRailBlock;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.random.Random;
import net.minecraft.world.World;
import net.minecraft.world.tick.TickPriority;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(AbstractRailBlock.class)
public abstract class AbstractRailBlockMixin extends AbstractBlockMixin {

    @Shadow protected abstract void updateBlockState(BlockState state, World world, BlockPos pos, Block neighbor);

    @Unique private Block neighbor;

    @WrapOperation(method = "updateBlockState(Lnet/minecraft/world/World;Lnet/minecraft/util/math/BlockPos;Lnet/minecraft/block/BlockState;Z)Lnet/minecraft/block/BlockState;", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/World;isReceivingRedstonePower(Lnet/minecraft/util/math/BlockPos;)Z"))
    private boolean allowQuasiConnectivity(World instance, BlockPos pos, Operation<Boolean> original, @Local(argsOnly = true) BlockState state) {
        Block block = state.getBlock();
        return TinkerKit.isReceivingRedstonePower(instance, pos, block);
    }

    @WrapWithCondition(method = "updateCurves", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/World;updateNeighbor(Lnet/minecraft/block/BlockState;Lnet/minecraft/util/math/BlockPos;Lnet/minecraft/block/Block;Lnet/minecraft/util/math/BlockPos;Z)V"))
    private boolean shouldUpdateNeighbours_onUpdateCurves(World instance, BlockState state_, BlockPos pos, Block sourceBlock, BlockPos sourcePos, boolean notify, @Local(argsOnly = true) BlockState state) {
        Block block = state.getBlock();
        return TinkerKit.shouldUpdateNeighbours(block);
    }

    @WrapWithCondition(method = "onStateReplaced", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/World;updateNeighborsAlways(Lnet/minecraft/util/math/BlockPos;Lnet/minecraft/block/Block;)V"))
    private boolean shouldUpdateNeighbours_onStateReplaced(World instance, BlockPos pos, Block sourceBlock, @Local(argsOnly = true, ordinal = 0) BlockState state) {
        Block block = state.getBlock();
        return TinkerKit.shouldUpdateNeighbours(block);
    }

    @WrapOperation(method = "neighborUpdate", at = @At(value = "INVOKE", target = "Lnet/minecraft/block/AbstractRailBlock;updateBlockState(Lnet/minecraft/block/BlockState;Lnet/minecraft/world/World;Lnet/minecraft/util/math/BlockPos;Lnet/minecraft/block/Block;)V"))
    private void scheduleOrCall(AbstractRailBlock instance, BlockState state, World world, BlockPos pos, Block neighbor, Operation<Void> original) {
        int delay = TinkerKit.getDelay(instance, 0);

        if (delay > 0) {
            this.neighbor = neighbor;

            Block block = state.getBlock();
            TickPriority tickPriority = TinkerKit.getTickPriority(instance);

            world.scheduleBlockTick(pos, block, delay, tickPriority);
        }
        else original.call(instance, state, world, pos, neighbor);
    }

    @Override
    public void scheduledTickImpl(BlockState state, ServerWorld world, BlockPos pos, Random random, CallbackInfo ci) {
        this.updateBlockState(state, world, pos, this.neighbor);
    }

}
