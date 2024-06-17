package com.axialeaa.doormat.mixin.tinker_kit.quasiconnectivity;

import com.axialeaa.doormat.tinker_kit.TinkerKit;
import com.bawnorton.mixinsquared.TargetHandler;
import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import net.minecraft.block.BlockState;
import net.minecraft.block.DispenserBlock;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.RedstoneView;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

@Mixin(value = DispenserBlock.class, priority = 1500)
public class DispenserBlockMixin {

    /**
     * This needs to be different because of carpet's modified quasi-connectivity logic.
     */
    @SuppressWarnings("UnresolvedMixinReference")
    @TargetHandler(mixin = "carpet.mixins.DispenserBlock_qcMixin", name = "carpet_hasQuasiSignal")
    @WrapOperation(method = "@MixinSquared:Handler", at = @At(value = "INVOKE", target =  "Lcarpet/helpers/QuasiConnectivity;hasQuasiSignal(Lnet/minecraft/world/RedstoneView;Lnet/minecraft/util/math/BlockPos;)Z"))
    private boolean shouldQC(RedstoneView instance, BlockPos pos, Operation<Boolean> original) {
        BlockState blockState = instance.getBlockState(pos);

        if (!TinkerKit.Type.QC.canModify(blockState.getBlock()))
            return original.call(instance, pos);

        int qcValue = (int) TinkerKit.Type.QC.getModifiedValue(blockState.getBlock());

        if (qcValue < 1)
            return false;

        for (int i = 1; i <= qcValue; i++) {
            BlockPos blockPos = pos.up(i);

            if (TinkerKit.cannotQC(instance, blockPos))
                break;

            if (instance.isReceivingRedstonePower(blockPos))
                return true;
        }

        return false;
    }

}
