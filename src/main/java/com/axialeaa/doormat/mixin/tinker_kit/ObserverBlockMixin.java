package com.axialeaa.doormat.mixin.tinker_kit;

import com.axialeaa.doormat.tinker_kit.TinkerKit;
import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import com.llamalad7.mixinextras.sugar.Local;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.ObserverBlock;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.random.Random;
import net.minecraft.world.WorldAccess;
import net.minecraft.world.tick.TickPriority;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyArg;

@Mixin(ObserverBlock.class)
public abstract class ObserverBlockMixin {

    @Shadow protected abstract void scheduledTick(BlockState state, ServerWorld world, BlockPos pos, Random random);

    @WrapOperation(method = "scheduleTick", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/WorldAccess;scheduleBlockTick(Lnet/minecraft/util/math/BlockPos;Lnet/minecraft/block/Block;I)V"))
    private void changeDelayAndTickPriority(WorldAccess instance, BlockPos pos, Block block, int i, Operation<Void> original) {
        BlockState blockState = instance.getBlockState(pos);
        int delay = TinkerKit.getDelay(blockState, i);

        if (delay == 0) {
            if (instance instanceof ServerWorld serverWorld)
                this.scheduledTick(blockState, serverWorld, pos, serverWorld.getRandom());
        } else {
            TickPriority tickPriority = TinkerKit.getTickPriority(blockState, TickPriority.NORMAL);
            instance.scheduleBlockTick(pos, block, delay, tickPriority);
        }
    }

    @WrapOperation(method = "scheduledTick", at = @At(value = "INVOKE", target = "Lnet/minecraft/server/world/ServerWorld;scheduleBlockTick(Lnet/minecraft/util/math/BlockPos;Lnet/minecraft/block/Block;I)V"))
    private void changeDelayAndTickPriority(ServerWorld instance, BlockPos pos, Block block, int i, Operation<Void> original, @Local(argsOnly = true) BlockState state) {
        int delay = Math.max(TinkerKit.getDelay(state, i), 1);
        TickPriority tickPriority = TinkerKit.getTickPriority(state, TickPriority.NORMAL);

        instance.scheduleBlockTick(pos, block, delay, tickPriority);
    }

    @ModifyArg(method = "scheduledTick", at = @At(value = "INVOKE", target = "Lnet/minecraft/server/world/ServerWorld;setBlockState(Lnet/minecraft/util/math/BlockPos;Lnet/minecraft/block/BlockState;I)Z"))
    private int changeUpdateType_onScheduledTick(int original, @Local(argsOnly = true) BlockState state) {
        return TinkerKit.getFlags(state, original);
    }

    @ModifyArg(method = "onBlockAdded", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/World;setBlockState(Lnet/minecraft/util/math/BlockPos;Lnet/minecraft/block/BlockState;I)Z"))
    private int changeUpdateType_onBlockAdded(int flags, @Local(argsOnly = true, ordinal = 0) BlockState state) {
        return TinkerKit.getFlags(state, flags);
    }

}
