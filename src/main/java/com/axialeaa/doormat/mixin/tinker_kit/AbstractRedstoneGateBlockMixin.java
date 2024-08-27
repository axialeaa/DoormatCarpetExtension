package com.axialeaa.doormat.mixin.tinker_kit;

import com.axialeaa.doormat.tinker_kit.TinkerKit;
import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import com.llamalad7.mixinextras.sugar.Local;
import net.minecraft.block.AbstractRedstoneGateBlock;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.HorizontalFacingBlock;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.util.math.random.Random;
import net.minecraft.world.World;
import net.minecraft.world.tick.TickPriority;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyArg;

@Mixin(value = AbstractRedstoneGateBlock.class, priority = 1500)
public abstract class AbstractRedstoneGateBlockMixin extends HorizontalFacingBlock {

    @Shadow protected abstract void scheduledTick(BlockState state, ServerWorld world, BlockPos pos, Random random);
    @Shadow protected abstract int getUpdateDelayInternal(BlockState state);

    protected AbstractRedstoneGateBlockMixin(Settings settings) {
        super(settings);
    }

    @WrapOperation(method = "getPower", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/World;getEmittedRedstonePower(Lnet/minecraft/util/math/BlockPos;Lnet/minecraft/util/math/Direction;)I"))
    private int allowQuasiConnectivity(World instance, BlockPos pos, Direction direction, Operation<Integer> original, @Local(argsOnly = true) BlockState state) {
        Block block = state.getBlock();
        return TinkerKit.getEmittedRedstonePower(instance, pos, block, direction);
    }

    @ModifyArg(method = "scheduledTick", at = @At(value = "INVOKE", target = "Lnet/minecraft/server/world/ServerWorld;setBlockState(Lnet/minecraft/util/math/BlockPos;Lnet/minecraft/block/BlockState;I)Z"))
    private int modifyUpdateType(int original, @Local(argsOnly = true) BlockState state) {
        Block block = state.getBlock();
        return TinkerKit.getFlags(block, original);
    }

    @WrapOperation(method = "scheduledTick", at = @At(value = "INVOKE", target = "Lnet/minecraft/server/world/ServerWorld;scheduleBlockTick(Lnet/minecraft/util/math/BlockPos;Lnet/minecraft/block/Block;ILnet/minecraft/world/tick/TickPriority;)V"))
    private void scheduleOrCall_scheduledTick(ServerWorld instance, BlockPos pos, Block block, int i, TickPriority tickPriority, Operation<Void> original, @Local(argsOnly = true) BlockState state, @Local(argsOnly = true) Random random) {
        int delay = this.getUpdateDelayInternal(state);

        if (delay > 0)
            instance.scheduleBlockTick(pos, block, delay, tickPriority);
        else this.scheduledTick(state, instance, pos, random);
    }

    @WrapOperation(method = "updatePowered", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/World;scheduleBlockTick(Lnet/minecraft/util/math/BlockPos;Lnet/minecraft/block/Block;ILnet/minecraft/world/tick/TickPriority;)V"))
    private void scheduleOrCall_updatePowered(World instance, BlockPos pos, Block block, int i, TickPriority tickPriority, Operation<Void> original, @Local(argsOnly = true) BlockState state) {
        int delay = this.getUpdateDelayInternal(state);

        if (delay > 0)
            instance.scheduleBlockTick(pos, block, delay, tickPriority);
        else if (instance instanceof ServerWorld serverWorld)
            this.scheduledTick(state, serverWorld, pos, serverWorld.getRandom());
    }

}
