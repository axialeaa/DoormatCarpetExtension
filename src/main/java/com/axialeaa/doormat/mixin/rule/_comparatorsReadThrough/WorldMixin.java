package com.axialeaa.doormat.mixin.rule._comparatorsReadThrough;

import com.axialeaa.doormat.fakes.BlockComparatorBehaviorInterface;
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

@Mixin(World.class)
public abstract class WorldMixin {

    @Shadow public abstract BlockState getBlockState(BlockPos pos);

    @SuppressWarnings("unused")
    @ModifyExpressionValue(method = "updateComparators", at = @At(value = "INVOKE", target = "Lnet/minecraft/block/BlockState;isSolidBlock(Lnet/minecraft/world/BlockView;Lnet/minecraft/util/math/BlockPos;)Z"))
    private boolean modifyBlockCheck(boolean original, BlockPos pos, Block block, @Local(ordinal = 0) Direction direction) {
        Block block1 = this.getBlockState(pos.offset(direction)).getBlock();
        if (block1 instanceof BlockComparatorBehaviorInterface comparatorBehaviorInterface)
            return comparatorBehaviorInterface.canReadThrough(block1) || original;
        return original;
    }

}
