package com.axialeaa.doormat.mixin.rule.fluidFlowSpeed;

import com.axialeaa.doormat.setting.DoormatSettings;
import com.llamalad7.mixinextras.injector.ModifyReturnValue;
import net.minecraft.fluid.WaterFluid;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

@Mixin(WaterFluid.class)
public class WaterFluidMixin {

    @ModifyReturnValue(method = "getTickRate", at = @At("RETURN"))
    private int modifySpeed(int original) {
        return DoormatSettings.waterFlowSpeed;
    }

}
