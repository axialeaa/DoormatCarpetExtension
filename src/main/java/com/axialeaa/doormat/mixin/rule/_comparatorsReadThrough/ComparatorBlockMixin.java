package com.axialeaa.doormat.mixin.rule._comparatorsReadThrough;

import com.axialeaa.doormat.fakes.BlockComparatorBehaviorInterface;
import com.llamalad7.mixinextras.injector.ModifyExpressionValue;
import com.llamalad7.mixinextras.sugar.Local;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.ComparatorBlock;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Debug;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

@Debug(export = true)
@Mixin(ComparatorBlock.class)
public class ComparatorBlockMixin {

    @SuppressWarnings("unused")
    @ModifyExpressionValue(method = "getPower", at = @At(value = "INVOKE", target = "Lnet/minecraft/block/BlockState;isSolidBlock(Lnet/minecraft/world/BlockView;Lnet/minecraft/util/math/BlockPos;)Z"))
    private boolean modifyBlockCheck(boolean original, World world, BlockPos pos, BlockState state, @Local(ordinal = 0) Direction direction) {
        Block block = world.getBlockState(pos.offset(direction)).getBlock();
        if (block instanceof BlockComparatorBehaviorInterface comparatorBehaviorInterface)
            return comparatorBehaviorInterface.canReadThrough(block) || original;
        return original;
    }

}
