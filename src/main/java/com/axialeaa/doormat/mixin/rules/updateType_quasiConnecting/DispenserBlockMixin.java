package com.axialeaa.doormat.mixin.rules.updateType_quasiConnecting;

import com.axialeaa.doormat.DoormatSettings;
import com.bawnorton.mixinsquared.TargetHandler;
import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import net.minecraft.block.DispenserBlock;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.RedstoneView;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyArg;

@Mixin(value = DispenserBlock.class, priority = 1500)
public class DispenserBlockMixin {

    @ModifyArg(method = "neighborUpdate", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/World;setBlockState(Lnet/minecraft/util/math/BlockPos;Lnet/minecraft/block/BlockState;I)Z"))
    private int changeUpdateType(int flags) {
        return DoormatSettings.dispenserUpdateType.getFlags();
    }

    /**
     * This needs to be different because of carpet's modified quasi-connectivity logic.
     */
    @TargetHandler(mixin = "carpet.mixins.DispenserBlock_qcMixin", name = "carpet_hasQuasiSignal")
    @WrapOperation(method = "@MixinSquared:Handler", at = @At(value = "INVOKE", target =  "Lcarpet/helpers/QuasiConnectivity;hasQuasiSignal(Lnet/minecraft/world/RedstoneView;Lnet/minecraft/util/math/BlockPos;)Z"))
    private boolean disableCarpetQC(RedstoneView world, BlockPos pos, Operation<Boolean> original) {
        return original.call(world, pos) && DoormatSettings.dispenserQuasiConnecting;
    }

}
