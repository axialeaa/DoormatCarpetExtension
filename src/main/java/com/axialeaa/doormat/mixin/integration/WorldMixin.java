package com.axialeaa.doormat.mixin.integration;

import com.axialeaa.doormat.interfaces.BlockComparatorBehaviourInterface;
import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import net.minecraft.block.BlockState;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.BlockView;
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

    @WrapOperation(method = "updateComparators", at = @At(value = "INVOKE", target = "Lnet/minecraft/block/BlockState;isSolidBlock(Lnet/minecraft/world/BlockView;Lnet/minecraft/util/math/BlockPos;)Z"))
    private boolean modifyBlockCheck(BlockState state, BlockView world, BlockPos pos, Operation<Boolean> original) {
        return original.call(state, world, pos) ||
            state.getBlock() instanceof BlockComparatorBehaviourInterface BCBI && BCBI.doormat$canReadThrough(state.getBlock());
    }

}
