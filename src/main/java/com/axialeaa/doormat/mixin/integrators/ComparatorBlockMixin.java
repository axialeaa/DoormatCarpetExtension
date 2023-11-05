package com.axialeaa.doormat.mixin.integrators;

import com.axialeaa.doormat.interfaces.BlockComparatorBehaviourInterface;
import com.llamalad7.mixinextras.injector.ModifyExpressionValue;
import com.llamalad7.mixinextras.sugar.Local;
import net.minecraft.block.Block;
import net.minecraft.block.ComparatorBlock;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

/**
 * This mixin (alongside {@link WorldMixin}) causes the behaviour that implementing {@link BlockComparatorBehaviourInterface} on a class creates.
 */
@Mixin(ComparatorBlock.class)
public class ComparatorBlockMixin {

    @ModifyExpressionValue(method = "getPower", at = @At(value = "INVOKE", target = "Lnet/minecraft/block/BlockState;isSolidBlock(Lnet/minecraft/world/BlockView;Lnet/minecraft/util/math/BlockPos;)Z"))
    private boolean modifyBlockCheck(boolean original, World world, BlockPos pos, @Local(ordinal = 0) Direction direction) {
        Block blockBehind = world.getBlockState(pos.offset(direction)).getBlock();
        return blockBehind instanceof BlockComparatorBehaviourInterface comparatorBehaviourInterface ?
            // if the block behind the comparator has the BlockComparatorBehaviourInterface implemented...
            original || comparatorBehaviourInterface.canReadThrough(blockBehind) :
            // return true if the block is a solid block or the interface method is true
            original; // otherwise return the output of the solid block check
    }

}
