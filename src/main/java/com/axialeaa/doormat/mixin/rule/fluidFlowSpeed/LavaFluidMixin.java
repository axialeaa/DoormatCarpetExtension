package com.axialeaa.doormat.mixin.rule.fluidFlowSpeed;

import com.axialeaa.doormat.DoormatSettings;
import com.llamalad7.mixinextras.injector.ModifyReturnValue;
import net.minecraft.fluid.LavaFluid;
import net.minecraft.world.WorldView;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

@Mixin(LavaFluid.class)
public class LavaFluidMixin {

    @ModifyReturnValue(method = "getTickRate", at = @At("RETURN"))
    private int modifySpeed(int original, WorldView world) {
        return world.getDimension().ultrawarm() ? DoormatSettings.lavaFlowSpeedNether : DoormatSettings.lavaFlowSpeedDefault;
    }


}
