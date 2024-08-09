package com.axialeaa.doormat.mixin.tinker_kit;

import com.axialeaa.doormat.tinker_kit.TinkerKit;
import com.llamalad7.mixinextras.injector.ModifyReturnValue;
import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import com.llamalad7.mixinextras.sugar.Local;
import net.minecraft.block.AbstractRedstoneGateBlock;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.HorizontalFacingBlock;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.random.Random;
import net.minecraft.world.World;
import net.minecraft.world.tick.TickPriority;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyArg;

@Mixin(AbstractRedstoneGateBlock.class)
public abstract class AbstractRedstoneGateBlockMixin {

    @Shadow protected abstract void scheduledTick(BlockState state, ServerWorld world, BlockPos pos, Random random);
    @Shadow protected abstract int getUpdateDelayInternal(BlockState state);

    @WrapOperation(method = "scheduledTick", at = @At(value = "INVOKE", target = "Lnet/minecraft/server/world/ServerWorld;scheduleBlockTick(Lnet/minecraft/util/math/BlockPos;Lnet/minecraft/block/Block;ILnet/minecraft/world/tick/TickPriority;)V"))
    private void changeDelayAndTickPriority_scheduledTick(ServerWorld instance, BlockPos pos, Block block, int i, TickPriority tickPriority, Operation<Void> original, @Local(argsOnly = true) BlockState state, @Local(argsOnly = true) Random random) {
        if (this.getUpdateDelayInternal(state) == 0)
            this.scheduledTick(state, instance, pos, random);
        else instance.scheduleBlockTick(pos, block, TinkerKit.getDelay(state, i), tickPriority);
    }

    @WrapOperation(method = "updatePowered", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/World;scheduleBlockTick(Lnet/minecraft/util/math/BlockPos;Lnet/minecraft/block/Block;ILnet/minecraft/world/tick/TickPriority;)V"))
    private void changeDelayAndTickPriority_updatePowered(World instance, BlockPos pos, Block block, int i, TickPriority tickPriority, Operation<Void> original, @Local(argsOnly = true) BlockState state) {
        if (this.getUpdateDelayInternal(state) == 0) {
            if (instance instanceof ServerWorld serverWorld)
                this.scheduledTick(state, serverWorld, pos, serverWorld.getRandom());
        }
        else instance.scheduleBlockTick(pos, block, TinkerKit.getDelay(state, i), tickPriority);
    }

    @ModifyReturnValue(method = "getPower", at = @At(value = "RETURN", ordinal = 1))
    private int allowQuasiConnectivity(int original, @Local(argsOnly = true) World world, @Local(argsOnly = true) BlockPos pos, @Local(argsOnly = true) BlockState state) {
        return TinkerKit.getEmittedRedstonePower(world, pos, state, state.get(HorizontalFacingBlock.FACING));
    }

    @ModifyArg(method = "scheduledTick", at = @At(value = "INVOKE", target = "Lnet/minecraft/server/world/ServerWorld;setBlockState(Lnet/minecraft/util/math/BlockPos;Lnet/minecraft/block/BlockState;I)Z"))
    private int changeUpdateType(int original, @Local(argsOnly = true) BlockState state) {
        return TinkerKit.getFlags(state, original);
    }

}
