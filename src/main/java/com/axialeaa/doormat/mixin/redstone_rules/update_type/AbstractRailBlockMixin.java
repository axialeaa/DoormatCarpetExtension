package com.axialeaa.doormat.mixin.redstone_rules.update_type;

import com.axialeaa.doormat.helpers.RedstoneRuleHelper;
import com.axialeaa.doormat.util.RedstoneRule;
import com.llamalad7.mixinextras.injector.WrapWithCondition;
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
    private boolean disableNeighborUpdates_updateCurves(World world, BlockState state, BlockPos pos, Block sourceBlock, BlockPos sourcePos, boolean notify) {
        return RedstoneRuleHelper.shouldUpdateNeighbours(RedstoneRule.RAIL);
    }

    @WrapWithCondition(method = "onStateReplaced", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/World;updateNeighborsAlways(Lnet/minecraft/util/math/BlockPos;Lnet/minecraft/block/Block;)V"))
    private boolean disableNeighborUpdates_onStateReplaced(World world, BlockPos pos, Block sourceBlock) {
        return RedstoneRuleHelper.shouldUpdateNeighbours(RedstoneRule.RAIL);
    }

}
