package com.axialeaa.doormat.mixin.redstone_rules.quasiconnectivity;

import com.axialeaa.doormat.util.QuasiConnectivityRules;
import com.bawnorton.mixinsquared.TargetHandler;
import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
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
    private boolean disableCarpetQC(RedstoneView world, BlockPos pos, Operation<Boolean> original) {
        return original.call(world, pos) && QuasiConnectivityRules.ruleValues.get(QuasiConnectivityRules.DISPENSER);
    }

}
