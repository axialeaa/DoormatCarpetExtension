package com.axialeaa.doormat.mixin.rule.comparatorsReadThrough.impl;

import com.axialeaa.doormat.util.ComparatorSolidBlock;
import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import net.minecraft.block.BlockState;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.BlockView;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

@Mixin(World.class)
public class WorldMixin {

    @WrapOperation(method = "updateComparators", at = @At(value = "INVOKE", target = "Lnet/minecraft/block/BlockState;isSolidBlock(Lnet/minecraft/world/BlockView;Lnet/minecraft/util/math/BlockPos;)Z"))
    private boolean shouldReadThrough(BlockState instance, BlockView world, BlockPos pos, Operation<Boolean> original) {
        return original.call(instance, world, pos) || ComparatorSolidBlock.isValidInstance(instance);
    }

}
