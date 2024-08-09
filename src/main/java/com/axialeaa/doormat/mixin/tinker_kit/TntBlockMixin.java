package com.axialeaa.doormat.mixin.tinker_kit;

import com.axialeaa.doormat.fake.TinkerKitBehaviourSetter;
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
public abstract class TntBlockMixin extends AbstractBlockMixin implements TinkerKitBehaviourSetter {

    @Shadow public static void primeTnt(World world, BlockPos pos) {}

    @Unique
    private void doBehaviour(BlockState state, World world, BlockPos pos) {
        int delay = TinkerKit.getDelay(state, 0);

        if (TinkerKit.isReceivingRedstonePower(world, pos, state)) {
            if (delay == 0)
                getBehaviour(world, pos, state);
            else world.scheduleBlockTick(pos, state.getBlock(), delay, TinkerKit.getTickPriority(state, TickPriority.NORMAL));
        }
    }

    @Inject(method = "neighborUpdate", at = @At("HEAD"), cancellable = true)
    private void onNeighborUpdate(BlockState state, World world, BlockPos pos, Block sourceBlock, BlockPos sourcePos, boolean notify, CallbackInfo ci) {
        doBehaviour(state, world, pos);
        ci.cancel();
    }

    @Inject(method = "onBlockAdded", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/World;isReceivingRedstonePower(Lnet/minecraft/util/math/BlockPos;)Z", shift = At.Shift.BEFORE), cancellable = true)
    private void onOnBlockAdded(BlockState state, World world, BlockPos pos, BlockState oldState, boolean notify, CallbackInfo ci) {
        doBehaviour(state, world, pos);
        ci.cancel();
    }

    @WrapOperation(method = "onProjectileHit", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/World;removeBlock(Lnet/minecraft/util/math/BlockPos;Z)Z"))
    private boolean changeUpdateTypeOnProjectileHit(World instance, BlockPos pos, boolean move, Operation<Boolean> original, @Local(argsOnly = true, ordinal = 0) BlockState state) {
        return TinkerKit.removeBlock(instance, pos, state, move);
    }

    @ModifyArg(method = "onUseWithItem", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/World;setBlockState(Lnet/minecraft/util/math/BlockPos;Lnet/minecraft/block/BlockState;I)Z"))
    private int changeUpdateTypeOnUseWithItem(int original, @Local(argsOnly = true) BlockState state) {
        return TinkerKit.getFlags(state, original) | Block.REDRAW_ON_MAIN_THREAD;
    }

    @Override
    public void scheduledTickImpl(BlockState state, ServerWorld world, BlockPos pos, Random random, CallbackInfo ci) {
        getBehaviour(world, pos, state);
    }

    @Override
    public void getBehaviour(World world, BlockPos pos, BlockState state) {
        primeTnt(world, pos);
        TinkerKit.removeBlock(world, pos, state, false);
    }

}
