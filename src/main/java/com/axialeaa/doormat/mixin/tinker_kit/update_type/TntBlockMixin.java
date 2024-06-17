package com.axialeaa.doormat.mixin.tinker_kit.update_type;

import com.axialeaa.doormat.tinker_kit.TinkerKit;
import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import com.llamalad7.mixinextras.sugar.Local;
import net.minecraft.block.BlockState;
import net.minecraft.block.TntBlock;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyArg;

@Mixin(TntBlock.class)
public class TntBlockMixin {

    @WrapOperation(method = { "onBlockAdded", "neighborUpdate", "onProjectileHit" }, at = @At(value = "INVOKE", target = "Lnet/minecraft/world/World;removeBlock(Lnet/minecraft/util/math/BlockPos;Z)Z"))
    private boolean changeUpdateType(World instance, BlockPos pos, boolean move, Operation<Boolean> original, @Local(argsOnly = true, ordinal = 0) BlockState state) {
        return TinkerKit.removeBlock(instance, pos, state, move);
    }

    @ModifyArg(method = "onUseWithItem", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/World;setBlockState(Lnet/minecraft/util/math/BlockPos;Lnet/minecraft/block/BlockState;I)Z"))
    private int changeUpdateType(BlockPos pos, BlockState state, int flags) {
        return TinkerKit.getFlags(state, flags);
    }

}
