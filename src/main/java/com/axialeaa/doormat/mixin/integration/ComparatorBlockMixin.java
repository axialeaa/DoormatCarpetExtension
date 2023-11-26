package com.axialeaa.doormat.mixin.integration;

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

    /**
     * @return true if the block to read through is solid or enabled by the interface, or the original solid block check if the block in question does not have the interface implemented.
     */
    @ModifyExpressionValue(method = "getPower", at = @At(value = "INVOKE", target = "Lnet/minecraft/block/BlockState;isSolidBlock(Lnet/minecraft/world/BlockView;Lnet/minecraft/util/math/BlockPos;)Z"))
    private boolean modifyBlockCheck(boolean original, World world, BlockPos pos, @Local(ordinal = 0) Direction direction) {
        Block blockBehind = world.getBlockState(pos.offset(direction)).getBlock();
        return original || blockBehind instanceof BlockComparatorBehaviourInterface BCBI && BCBI.doormat$canReadThrough(blockBehind);
    }

}
