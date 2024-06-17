package com.axialeaa.doormat.mixin.tinker_kit.quasiconnectivity;

import com.axialeaa.doormat.tinker_kit.TinkerKit;
import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import com.llamalad7.mixinextras.sugar.Local;
import net.minecraft.block.BlockState;
import net.minecraft.block.DoorBlock;
import net.minecraft.block.enums.DoubleBlockHalf;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

@Mixin(DoorBlock.class)
public class DoorBlockMixin {

    /**
     * This is weird because doors are two blocks tall, and both halves need to activate at the same time.
     */
    @WrapOperation(method = "neighborUpdate", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/World;isReceivingRedstonePower(Lnet/minecraft/util/math/BlockPos;)Z", ordinal = 0))
    private boolean shouldQC(World instance, BlockPos pos, Operation<Boolean> original, @Local(argsOnly = true) BlockState state) {
        boolean isLowerHalf = state.get(DoorBlock.HALF) == DoubleBlockHalf.LOWER;

        return TinkerKit.isReceivingRedstonePower(instance, pos, state, isLowerHalf ? 1 : 0);
    }

}
