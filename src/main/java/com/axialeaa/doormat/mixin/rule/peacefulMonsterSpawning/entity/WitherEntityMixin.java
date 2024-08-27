package com.axialeaa.doormat.mixin.rule.peacefulMonsterSpawning.entity;

import com.axialeaa.doormat.settings.DoormatSettings;
import com.llamalad7.mixinextras.injector.ModifyExpressionValue;
import net.minecraft.entity.boss.WitherEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

@Mixin(WitherEntity.class)
public class WitherEntityMixin {

    /**
     * Ignores the value of isDisallowedInPeaceful() when deciding if this wither should despawn.
     */
    @ModifyExpressionValue(method = "checkDespawn", at = @At(value = "INVOKE", target = "Lnet/minecraft/entity/boss/WitherEntity;isDisallowedInPeaceful()Z"))
    private boolean bypassPeacefulDespawnCheck(boolean original) {
        return !DoormatSettings.peacefulMonsterSpawning.isEnabled();
    }

}
