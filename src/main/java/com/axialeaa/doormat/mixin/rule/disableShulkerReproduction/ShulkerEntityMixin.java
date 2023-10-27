package com.axialeaa.doormat.mixin.rule.disableShulkerReproduction;

import com.axialeaa.doormat.DoormatSettings;
import com.llamalad7.mixinextras.injector.WrapWithCondition;
import net.minecraft.entity.mob.ShulkerEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

@SuppressWarnings("unused")
@Mixin(ShulkerEntity.class)
public class ShulkerEntityMixin {

    @WrapWithCondition(method = "damage", at = @At(value = "INVOKE", target = "Lnet/minecraft/entity/mob/ShulkerEntity;spawnNewShulker()V"))
    private boolean disableReproduction(ShulkerEntity shulker) {
        return !DoormatSettings.disableShulkerReproduction;
    }

}
