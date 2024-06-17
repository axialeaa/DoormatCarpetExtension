package com.axialeaa.doormat.mixin.tinker_kit.update_type;

import com.axialeaa.doormat.tinker_kit.TinkerKit;
import com.llamalad7.mixinextras.injector.v2.WrapWithCondition;
import com.llamalad7.mixinextras.sugar.Local;
import net.minecraft.block.AbstractRailBlock;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

@Mixin(AbstractRailBlock.class)
public class AbstractRailBlockMixin {

    @WrapWithCondition(method = "updateCurves", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/World;updateNeighbor(Lnet/minecraft/block/BlockState;Lnet/minecraft/util/math/BlockPos;Lnet/minecraft/block/Block;Lnet/minecraft/util/math/BlockPos;Z)V"))
    private boolean shouldUpdateNeighbours_updateCurves(World instance, BlockState state_, BlockPos pos, Block sourceBlock, BlockPos sourcePos, boolean notify, @Local(argsOnly = true) BlockState state) {
        return TinkerKit.shouldUpdateNeighbours(state);
    }

    @WrapWithCondition(method = "onStateReplaced", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/World;updateNeighborsAlways(Lnet/minecraft/util/math/BlockPos;Lnet/minecraft/block/Block;)V"))
    private boolean shouldUpdateNeighbours_onStateReplaced(World instance, BlockPos pos, Block sourceBlock, @Local(argsOnly = true, ordinal = 0) BlockState state) {
        return TinkerKit.shouldUpdateNeighbours(state);
    }

}
