package com.axialeaa.doormat.mixin.rule.dustTravel;

import com.axialeaa.doormat.fakes.BlockDustBehaviourInterface;
import com.llamalad7.mixinextras.injector.ModifyExpressionValue;
import com.llamalad7.mixinextras.sugar.Local;
import net.minecraft.block.BlockState;
import net.minecraft.block.RedstoneWireBlock;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

@Mixin(RedstoneWireBlock.class)
public class RedstoneWireBlockMixin {

    @SuppressWarnings("unused")
    @ModifyExpressionValue(method = "getReceivedRedstonePower", at = @At(value = "INVOKE", target = "Lnet/minecraft/block/BlockState;isSolidBlock(Lnet/minecraft/world/BlockView;Lnet/minecraft/util/math/BlockPos;)Z", ordinal = 0))
    private boolean modifyDescent(boolean original, World world, BlockPos pos, @Local Direction direction, @Local(ordinal = 0) BlockPos blockPos, @Local BlockState blockState) {
        if (blockState.getBlock() instanceof BlockDustBehaviourInterface dustBehaviorInterface)
            return original || dustBehaviorInterface.dustCanDescend(world, blockPos, blockState, direction);
        return original;
    }

}
