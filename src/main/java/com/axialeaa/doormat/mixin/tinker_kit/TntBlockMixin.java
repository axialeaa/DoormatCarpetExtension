package com.axialeaa.doormat.mixin.tinker_kit;

import com.axialeaa.doormat.mixin.extensibility.AbstractBlockMixin;
import com.axialeaa.doormat.tinker_kit.TinkerKit;
import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import com.llamalad7.mixinextras.sugar.Local;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.TntBlock;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.random.Random;
import net.minecraft.world.World;
import net.minecraft.world.tick.TickPriority;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.ModifyArg;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(TntBlock.class)
public abstract class TntBlockMixin extends AbstractBlockMixin {

    @Shadow public static void primeTnt(World world, BlockPos pos) {}

    @WrapOperation(method = "onProjectileHit", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/World;removeBlock(Lnet/minecraft/util/math/BlockPos;Z)Z"))
    private boolean modifyUpdateType_onProjectileHit(World instance, BlockPos pos, boolean move, Operation<Boolean> original, @Local(argsOnly = true, ordinal = 0) BlockState state) {
        Block block = state.getBlock();
        return TinkerKit.removeBlock(instance, pos, block, move);
    }

    @ModifyArg(method = "onUseWithItem", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/World;setBlockState(Lnet/minecraft/util/math/BlockPos;Lnet/minecraft/block/BlockState;I)Z"))
    private int modifyUpdateType_onUseWithItem(int original, @Local(argsOnly = true) BlockState state) {
        Block block = state.getBlock();
        return TinkerKit.getFlags(block, original) | Block.REDRAW_ON_MAIN_THREAD;
    }

    @Inject(method = "neighborUpdate", at = @At("HEAD"), cancellable = true)
    private void scheduleOrCall_neighborUpdate(BlockState state, World world, BlockPos pos, Block sourceBlock, BlockPos sourcePos, boolean notify, CallbackInfo ci) {
        scheduleOrCall(state, world, pos);
        ci.cancel();
    }

    @Inject(method = "onBlockAdded", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/World;isReceivingRedstonePower(Lnet/minecraft/util/math/BlockPos;)Z", shift = At.Shift.BEFORE), cancellable = true)
    private void scheduleOrCall_onBlockAdded(BlockState state, World world, BlockPos pos, BlockState oldState, boolean notify, CallbackInfo ci) {
        scheduleOrCall(state, world, pos);
        ci.cancel();
    }

    @Override
    public void scheduledTickImpl(BlockState state, ServerWorld world, BlockPos pos, Random random, CallbackInfo ci) {
        getBehaviour(world, pos, state);
    }

    @Unique
    private void scheduleOrCall(BlockState state, World world, BlockPos pos) {
        Block block = state.getBlock();
        int delay = TinkerKit.getDelay(block, 0);

        if (!TinkerKit.isReceivingRedstonePower(world, pos, block))
            return;

        if (delay > 0) {
            TickPriority tickPriority = TinkerKit.getTickPriority(block);
            world.scheduleBlockTick(pos, block, delay, tickPriority);
        }
        else getBehaviour(world, pos, state);
    }

    @Unique
    public void getBehaviour(World world, BlockPos pos, BlockState state) {
        primeTnt(world, pos);
        TinkerKit.removeBlock(world, pos, state.getBlock(), false);
    }

}
