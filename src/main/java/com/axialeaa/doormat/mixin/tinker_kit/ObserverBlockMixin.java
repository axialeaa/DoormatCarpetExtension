package com.axialeaa.doormat.mixin.tinker_kit;

import com.axialeaa.doormat.tinker_kit.TinkerKitUtils;
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

@Mixin(value = ObserverBlock.class, priority = 1500)
public abstract class ObserverBlockMixin {

    @Shadow protected abstract void scheduledTick(BlockState state, ServerWorld world, BlockPos pos, Random random);

    @ModifyArg(method = "scheduledTick", at = @At(value = "INVOKE", target = "Lnet/minecraft/server/world/ServerWorld;setBlockState(Lnet/minecraft/util/math/BlockPos;Lnet/minecraft/block/BlockState;I)Z"))
    private int modifyUpdateType_onScheduledTick(int original, @Local(argsOnly = true) BlockState state) {
        Block block = state.getBlock();
        return TinkerKitUtils.getFlags(block, original);
    }

    @ModifyArg(method = "onBlockAdded", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/World;setBlockState(Lnet/minecraft/util/math/BlockPos;Lnet/minecraft/block/BlockState;I)Z"))
    private int modifyUpdateType_onBlockAdded(int original, @Local(argsOnly = true, ordinal = 0) BlockState state) {
        Block block = state.getBlock();
        return TinkerKitUtils.getFlags(block, original) | Block.FORCE_STATE;
    }

    @WrapOperation(method = "scheduledTick", at = @At(value = "INVOKE", target = "Lnet/minecraft/server/world/ServerWorld;scheduleBlockTick(Lnet/minecraft/util/math/BlockPos;Lnet/minecraft/block/Block;I)V"))
    private void scheduleOrCall(ServerWorld instance, BlockPos pos, Block block, int i, Operation<Void> original) {
        int delay = Math.max(TinkerKitUtils.getDelay(block, i), 1);
        TickPriority tickPriority = TinkerKitUtils.getTickPriority(block);

        instance.scheduleBlockTick(pos, block, delay, tickPriority);
    }

    @WrapOperation(method = "scheduleTick", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/WorldAccess;scheduleBlockTick(Lnet/minecraft/util/math/BlockPos;Lnet/minecraft/block/Block;I)V"))
    private void scheduleOrCall(WorldAccess instance, BlockPos pos, Block block, int i, Operation<Void> original) {
        int delay = Math.max(TinkerKitUtils.getDelay(block, i), 1);
        TickPriority tickPriority = TinkerKitUtils.getTickPriority(block);

        instance.scheduleBlockTick(pos, block, delay, tickPriority);
    }

}
