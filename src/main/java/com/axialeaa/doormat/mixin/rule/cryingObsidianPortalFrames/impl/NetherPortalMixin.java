package com.axialeaa.doormat.mixin.rule.cryingObsidianPortalFrames.impl;

import com.axialeaa.doormat.util.NetherPortalFrameBlock;
import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import net.minecraft.block.AbstractBlock;
import net.minecraft.block.BlockState;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.BlockView;
import net.minecraft.world.dimension.NetherPortal;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

@Mixin(NetherPortal.class)
public class NetherPortalMixin {

    @WrapOperation(method = { "getWidth(Lnet/minecraft/util/math/BlockPos;Lnet/minecraft/util/math/Direction;)I", "isHorizontalFrameValid", "getPotentialHeight" }, at = @At(value = "INVOKE", target = "Lnet/minecraft/block/AbstractBlock$ContextPredicate;test(Lnet/minecraft/block/BlockState;Lnet/minecraft/world/BlockView;Lnet/minecraft/util/math/BlockPos;)Z"))
    private boolean isValidFrameBlock(AbstractBlock.ContextPredicate instance, BlockState state, BlockView world, BlockPos pos, Operation<Boolean> original) {
        return original.call(instance, state, world, pos) || NetherPortalFrameBlock.isValidInstance(state);
    }

}
