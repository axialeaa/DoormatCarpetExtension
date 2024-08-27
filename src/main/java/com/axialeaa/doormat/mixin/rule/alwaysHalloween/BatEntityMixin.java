package com.axialeaa.doormat.mixin.rule.alwaysHalloween;

import com.axialeaa.doormat.settings.DoormatSettings;
import com.llamalad7.mixinextras.injector.ModifyExpressionValue;
import net.minecraft.entity.passive.BatEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

@Mixin(BatEntity.class)
public class BatEntityMixin {

    @ModifyExpressionValue(method = "canSpawn", at = @At(value = "INVOKE", target = "Lnet/minecraft/entity/passive/BatEntity;isTodayAroundHalloween()Z"))
    private static boolean modifyFrequency(boolean original) {
        return DoormatSettings.alwaysHalloween || original;
    }

}
