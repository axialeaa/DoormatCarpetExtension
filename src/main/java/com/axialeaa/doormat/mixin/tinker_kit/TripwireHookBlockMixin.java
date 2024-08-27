package com.axialeaa.doormat.mixin.tinker_kit;

import com.axialeaa.doormat.tinker_kit.TinkerKit;
import com.llamalad7.mixinextras.injector.v2.WrapWithCondition;
import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import com.llamalad7.mixinextras.sugar.Local;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.TripwireHookBlock;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.random.Random;
import net.minecraft.world.World;
import net.minecraft.world.tick.TickPriority;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyArg;

@Mixin(TripwireHookBlock.class)
public abstract class TripwireHookBlockMixin {

    @Shadow protected abstract void scheduledTick(BlockState state, ServerWorld world, BlockPos pos, Random random);
    @Shadow public static void update(World world, BlockPos pos, BlockState state, boolean bl, boolean bl2, int i, @Nullable BlockState blockState) {}

    @ModifyArg(method = "update", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/World;setBlockState(Lnet/minecraft/util/math/BlockPos;Lnet/minecraft/block/BlockState;I)Z"))
    private static int modifyUpdateType(int original, @Local Block block) {
        return TinkerKit.getFlags(block, original);
    }

    @WrapWithCondition(method = "updateNeighborsOnAxis", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/World;updateNeighborsAlways(Lnet/minecraft/util/math/BlockPos;Lnet/minecraft/block/Block;)V"))
    private static boolean shouldUpdateNeighboursOnAxis(World instance, BlockPos pos, Block sourceBlock) {
        return TinkerKit.shouldUpdateNeighbours(sourceBlock);
    }

    @WrapWithCondition(method = "onStateReplaced", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/World;updateNeighborsAlways(Lnet/minecraft/util/math/BlockPos;Lnet/minecraft/block/Block;)V"))
    private boolean shouldUpdateNeighbours_onStateReplaced(World instance, BlockPos pos, Block sourceBlock) {
        return TinkerKit.shouldUpdateNeighbours(sourceBlock);
    }

    @WrapOperation(method = "update", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/World;scheduleBlockTick(Lnet/minecraft/util/math/BlockPos;Lnet/minecraft/block/Block;I)V"))
    private static void modifyDelay(World instance, BlockPos pos, Block block, int i, Operation<Void> original, @Local(argsOnly = true, ordinal = 0) BlockState state) {
        int delay = TinkerKit.getDelay(block, i);

        if (delay > 0) {
            TickPriority tickPriority = TinkerKit.getTickPriority(block);
            instance.scheduleBlockTick(pos, block, delay, tickPriority);
        }
        else update(instance, pos, state, false, true, -1, null);
    }

}
