package com.axialeaa.doormat.mixin.integration;

import com.axialeaa.doormat.helpers.ComparatorBehaviourHelper;
import com.axialeaa.doormat.interfaces.ComparatorBehaviourInterface;
import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import net.minecraft.block.BlockState;
import net.minecraft.block.ComparatorBlock;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.BlockView;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

/**
 * This mixin (alongside {@link WorldMixin}) causes the behaviour that implementing {@link ComparatorBehaviourInterface} on a class creates.
 */
@Mixin(ComparatorBlock.class)
public class ComparatorBlockMixin {

    /**
     * @return true if the block to read through is solid or enabled by the interface, or the original solid block check if the block in question does not have the interface implemented.
     */
    @WrapOperation(method = "getPower", at = @At(value = "INVOKE", target = "Lnet/minecraft/block/BlockState;isSolidBlock(Lnet/minecraft/world/BlockView;Lnet/minecraft/util/math/BlockPos;)Z"))
    private boolean modifyBlockCheck(BlockState state, BlockView world, BlockPos pos, Operation<Boolean> original) {
        return ComparatorBehaviourHelper.modifyBlockCheck(state, world, pos, original);
    }

}
