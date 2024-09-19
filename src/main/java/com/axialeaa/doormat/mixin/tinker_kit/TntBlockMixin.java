package com.axialeaa.doormat.mixin.tinker_kit;

import carpet.CarpetSettings;
import com.axialeaa.doormat.mixin.impl.AbstractBlockImplMixin;
import com.axialeaa.doormat.registry.DoormatTinkerTypes;
import com.axialeaa.doormat.tinker_kit.TinkerKitUtils;
import com.llamalad7.mixinextras.injector.wrapmethod.WrapMethod;
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
import org.spongepowered.asm.mixin.injection.ModifyArg;

@Mixin(value = TntBlock.class, priority = 1500)
public abstract class TntBlockMixin extends AbstractBlockImplMixin {

    @Shadow public static void primeTnt(World world, BlockPos pos) {}

    @WrapOperation(method = "onProjectileHit", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/World;removeBlock(Lnet/minecraft/util/math/BlockPos;Z)Z"))
    private boolean modifyUpdateType_onProjectileHit(World instance, BlockPos pos, boolean move, Operation<Boolean> original, @Local(argsOnly = true, ordinal = 0) BlockState state) {
        Block block = state.getBlock();
        return TinkerKitUtils.removeBlock(instance, pos, block, move);
    }

    @ModifyArg(method = "onUseWithItem", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/World;setBlockState(Lnet/minecraft/util/math/BlockPos;Lnet/minecraft/block/BlockState;I)Z"))
    private int modifyUpdateType_onUseWithItem(int original, @Local(argsOnly = true) BlockState state) {
        Block block = state.getBlock();
        return TinkerKitUtils.getFlags(block, original) | Block.REDRAW_ON_MAIN_THREAD;
    }

    @WrapMethod(method = "neighborUpdate")
    public void scheduleOrCall_neighborUpdate(BlockState state, World world, BlockPos pos, Block sourceBlock, BlockPos sourcePos, boolean notify, Operation<Void> original) {
        if (world.isClient())
            return;

        Block block = state.getBlock();

        if (!DoormatTinkerTypes.QC.canModify(block)) {
            original.call(state, world, pos, sourceBlock, sourcePos, notify);
            return;
        }

        this.scheduleOrCall(state, world, pos);
    }

    @WrapMethod(method = "onBlockAdded")
    public void scheduleOrCall_onBlockAdded(BlockState state, World world, BlockPos pos, BlockState oldState, boolean notify, Operation<Void> original) {
        if (world.isClient())
            return;

        Block block = state.getBlock();

        if (CarpetSettings.tntDoNotUpdate || oldState.isOf(block))
            return;

        if (!DoormatTinkerTypes.QC.canModify(block)) {
            original.call(state, world, pos, oldState, notify);
            return;
        }

        this.scheduleOrCall(state, world, pos);
    }

    @Override
    public void scheduledTickImpl(BlockState state, ServerWorld world, BlockPos pos, Random random, Operation<Void> original) {
        this.getBehaviour(world, pos, state);
        original.call(state, world, pos, random);
    }

    @Unique
    private void scheduleOrCall(BlockState state, World world, BlockPos pos) {
        Block block = state.getBlock();
        int delay = TinkerKitUtils.getDelay(block, 0);

        if (!TinkerKitUtils.isReceivingRedstonePower(world, pos, block))
            return;

        if (delay > 0) {
            TickPriority tickPriority = TinkerKitUtils.getTickPriority(block);
            world.scheduleBlockTick(pos, block, delay, tickPriority);
        }
        else this.getBehaviour(world, pos, state);
    }

    @Unique
    public void getBehaviour(World world, BlockPos pos, BlockState state) {
        primeTnt(world, pos);
        TinkerKitUtils.removeBlock(world, pos, state.getBlock(), false);
    }

}
