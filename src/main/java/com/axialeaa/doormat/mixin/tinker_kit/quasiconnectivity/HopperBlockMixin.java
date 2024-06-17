package com.axialeaa.doormat.mixin.tinker_kit.quasiconnectivity;

import com.axialeaa.doormat.tinker_kit.TinkerKit;
import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import com.llamalad7.mixinextras.sugar.Local;
import net.minecraft.block.BlockState;
import net.minecraft.block.HopperBlock;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

@Mixin(HopperBlock.class)
public class HopperBlockMixin {

    @WrapOperation(method = "updateEnabled", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/World;isReceivingRedstonePower(Lnet/minecraft/util/math/BlockPos;)Z"))
    private boolean allowQuasiConnecting(World instance, BlockPos pos, Operation<Boolean> original, @Local(argsOnly = true) BlockState state) {
        return TinkerKit.isReceivingRedstonePower(instance, pos, state);
    }

}
