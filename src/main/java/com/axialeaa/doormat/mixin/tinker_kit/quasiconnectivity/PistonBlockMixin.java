package com.axialeaa.doormat.mixin.tinker_kit.quasiconnectivity;

import com.axialeaa.doormat.tinker_kit.TinkerKit;
import com.bawnorton.mixinsquared.TargetHandler;
import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import net.minecraft.block.BlockState;
import net.minecraft.block.PistonBlock;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.RedstoneView;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

@Mixin(value = PistonBlock.class, priority = 1500)
public class PistonBlockMixin {

    /**
     * This needs to be different because of carpet's modified quasi-connectivity logic.
     */
    @SuppressWarnings({ "UnresolvedMixinReference", "MixinAnnotationTarget", "InvalidMemberReference" })
    @TargetHandler(mixin = "carpet.mixins.PistonBaseBlock_qcMixin", name = "carpet_checkQuasiSignal")
    @WrapOperation(method = "@MixinSquared:Handler", at = @At(value = "INVOKE", target =  "Lcarpet/helpers/QuasiConnectivity;hasQuasiSignal(Lnet/minecraft/world/RedstoneView;Lnet/minecraft/util/math/BlockPos;)Z"))
    private boolean shouldQC(RedstoneView instance, BlockPos pos, Operation<Boolean> original) {
        BlockState blockState = instance.getBlockState(pos);

        if (!TinkerKit.isModifiable(blockState.getBlock(), TinkerKit.ModificationType.QC))
            return original.call(instance, pos);

        if (TinkerKit.MODIFIED_QC_VALUES.get(blockState.getBlock()) < 1)
            return false;

        for (int i = 1; i <= TinkerKit.MODIFIED_QC_VALUES.get(blockState.getBlock()); i++) {
            BlockPos blockPos = pos.up(i);

            if (TinkerKit.cannotQC(instance, blockPos))
                break;

            if (instance.isReceivingRedstonePower(blockPos))
                return true;
        }
        return false;
    }

}
