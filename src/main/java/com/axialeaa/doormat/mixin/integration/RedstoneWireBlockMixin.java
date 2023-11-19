package com.axialeaa.doormat.mixin.integration;

import com.axialeaa.doormat.interfaces.BlockDustBehaviourInterface;
import com.llamalad7.mixinextras.injector.ModifyExpressionValue;
import com.llamalad7.mixinextras.sugar.Local;
import net.minecraft.block.BlockState;
import net.minecraft.block.RedstoneWireBlock;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

/**
 * This mixin causes the behaviour that implementing {@link BlockDustBehaviourInterface} on a class creates.
 */
@Mixin(RedstoneWireBlock.class)
public class RedstoneWireBlockMixin {

    /**
     * @return true if the block to descend is solid or enabled by the interface, or the original solid block check if the block in question does not have the interface implemented.
     */
    @ModifyExpressionValue(method = "getReceivedRedstonePower", at = @At(value = "INVOKE", target = "Lnet/minecraft/block/BlockState;isSolidBlock(Lnet/minecraft/world/BlockView;Lnet/minecraft/util/math/BlockPos;)Z", ordinal = 0))
    private boolean modifyDescent(boolean original, World world, @Local Direction direction, @Local(ordinal = 0) BlockPos blockPos, @Local BlockState blockState) {
        return blockState.getBlock() instanceof BlockDustBehaviourInterface dustBehaviorInterface ?
            original || dustBehaviorInterface.dustCanDescend(world, blockPos, blockState, direction) :
            original;
    }

}
