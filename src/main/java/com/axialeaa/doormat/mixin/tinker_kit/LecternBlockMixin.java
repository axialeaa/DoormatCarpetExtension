package com.axialeaa.doormat.mixin.tinker_kit;

import com.axialeaa.doormat.tinker_kit.TinkerKit;
import com.llamalad7.mixinextras.injector.v2.WrapWithCondition;
import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import com.llamalad7.mixinextras.sugar.Local;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.LecternBlock;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.tick.TickPriority;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyArg;

@Mixin(value = LecternBlock.class, priority = 1500)
public abstract class LecternBlockMixin {

    @Shadow private static void setPowered(World world, BlockPos pos, BlockState state, boolean powered) {}

    @ModifyArg(method = { "setHasBook", "setPowered(Lnet/minecraft/world/World;Lnet/minecraft/util/math/BlockPos;Lnet/minecraft/block/BlockState;Z)V" }, at = @At(value = "INVOKE", target = "Lnet/minecraft/world/World;setBlockState(Lnet/minecraft/util/math/BlockPos;Lnet/minecraft/block/BlockState;I)Z"))
    private static int modifyUpdateType(int original, @Local(argsOnly = true) BlockState state) {
        Block block = state.getBlock();
        return TinkerKit.getFlags(block, original);
    }

    @WrapWithCondition(method = "updateNeighborAlways", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/World;updateNeighborsAlways(Lnet/minecraft/util/math/BlockPos;Lnet/minecraft/block/Block;)V"))
    private static boolean shouldUpdateNeighbours(World instance, BlockPos pos, Block sourceBlock) {
        return TinkerKit.shouldUpdateNeighbours(sourceBlock);
    }

    @WrapOperation(method = "setPowered(Lnet/minecraft/world/World;Lnet/minecraft/util/math/BlockPos;Lnet/minecraft/block/BlockState;)V", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/World;scheduleBlockTick(Lnet/minecraft/util/math/BlockPos;Lnet/minecraft/block/Block;I)V"))
    private static void modifyDelay(World instance, BlockPos pos, Block block, int i, Operation<Void> original, @Local(argsOnly = true) BlockState state) {
        int delay = TinkerKit.getDelay(block, i);

        if (delay > 0) {
            TickPriority tickPriority = TinkerKit.getTickPriority(block);
            instance.scheduleBlockTick(pos, block, delay, tickPriority);
        }
        else setPowered(instance, pos, state, false);
    }

}
