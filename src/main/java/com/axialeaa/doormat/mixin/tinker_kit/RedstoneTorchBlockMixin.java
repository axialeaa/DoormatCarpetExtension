package com.axialeaa.doormat.mixin.tinker_kit;

import com.axialeaa.doormat.tinker_kit.TinkerKit;
import com.llamalad7.mixinextras.injector.v2.WrapWithCondition;
import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import com.llamalad7.mixinextras.sugar.Local;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.RedstoneTorchBlock;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.random.Random;
import net.minecraft.world.World;
import net.minecraft.world.tick.TickPriority;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyArg;

@Mixin(RedstoneTorchBlock.class)
public abstract class RedstoneTorchBlockMixin {

    @Shadow protected abstract void scheduledTick(BlockState state, ServerWorld world, BlockPos pos, Random random);

    @WrapOperation(method = "neighborUpdate", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/World;scheduleBlockTick(Lnet/minecraft/util/math/BlockPos;Lnet/minecraft/block/Block;I)V"))
    private void changeDelayAndTickPriority(World instance, BlockPos pos, Block block, int i, Operation<Void> original, @Local(argsOnly = true) BlockState state) {
        int delay = TinkerKit.getDelay(state, i);

        if (delay == 0) {
            if (instance instanceof ServerWorld serverWorld)
                this.scheduledTick(state, serverWorld, pos, instance.getRandom());
        } else {
            TickPriority tickPriority = TinkerKit.getTickPriority(state, TickPriority.NORMAL);
            instance.scheduleBlockTick(pos, block, delay, tickPriority);
        }
    }

    @WrapWithCondition(method = { "onBlockAdded", "onStateReplaced" }, at = @At(value = "INVOKE", target = "Lnet/minecraft/world/World;updateNeighborsAlways(Lnet/minecraft/util/math/BlockPos;Lnet/minecraft/block/Block;)V"))
    private boolean suppressUpdates(World instance, BlockPos pos, Block sourceBlock, @Local(argsOnly = true, ordinal = 0) BlockState state) {
        return TinkerKit.shouldUpdateNeighbours(state);
    }

    @ModifyArg(method = "scheduledTick", at = @At(value = "INVOKE", target = "Lnet/minecraft/server/world/ServerWorld;setBlockState(Lnet/minecraft/util/math/BlockPos;Lnet/minecraft/block/BlockState;I)Z"), index = 2)
    private int changeUpdateType(int original, @Local(argsOnly = true) BlockState state) {
        return TinkerKit.getFlags(state, original);
    }

}
