package com.axialeaa.doormat.mixin.integrators;

import com.axialeaa.doormat.interfaces.BlockComparatorBehaviourInterface;
import com.llamalad7.mixinextras.injector.ModifyExpressionValue;
import com.llamalad7.mixinextras.sugar.Local;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;

/**
 * This mixin (alongside {@link ComparatorBlockMixin}) causes the behaviour that implementing {@link BlockComparatorBehaviourInterface} on a class creates.
 */
@Mixin(World.class)
public abstract class WorldMixin {

    @Shadow public abstract BlockState getBlockState(BlockPos pos);

    @ModifyExpressionValue(method = "updateComparators", at = @At(value = "INVOKE", target = "Lnet/minecraft/block/BlockState;isSolidBlock(Lnet/minecraft/world/BlockView;Lnet/minecraft/util/math/BlockPos;)Z"))
    private boolean modifyBlockCheck(boolean original, BlockPos pos, @Local(ordinal = 0) Direction direction) {
        Block blockBehind = this.getBlockState(pos.offset(direction)).getBlock();
        return blockBehind instanceof BlockComparatorBehaviourInterface comparatorBehaviourInterface ?
            original || comparatorBehaviourInterface.canReadThrough(blockBehind) :
            original;
    }

}
