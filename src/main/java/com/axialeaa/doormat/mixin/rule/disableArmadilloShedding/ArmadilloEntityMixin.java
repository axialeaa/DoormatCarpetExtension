package com.axialeaa.doormat.mixin.rule.disableArmadilloShedding;

import com.axialeaa.doormat.setting.DoormatSettings;
import com.llamalad7.mixinextras.injector.ModifyExpressionValue;
import net.minecraft.entity.passive.ArmadilloEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

@Mixin(ArmadilloEntity.class)
public class ArmadilloEntityMixin {

    @ModifyExpressionValue(method = "mobTick", at = @At(value = "INVOKE", target = "Lnet/minecraft/entity/passive/ArmadilloEntity;isAlive()Z"))
    private boolean canShed(boolean original) {
        return !DoormatSettings.disableArmadilloShedding && original;
    }

}
