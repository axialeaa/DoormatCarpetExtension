package com.axialeaa.doormat.mixin.rule.peacefulMonsterSpawning.entity;

import com.axialeaa.doormat.setting.DoormatSettings;
import com.llamalad7.mixinextras.injector.ModifyExpressionValue;
import net.minecraft.entity.mob.MobEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

@Mixin(MobEntity.class)
public class MobEntityMixin {

    /**
     * When the rule is enabled, ignores the value of isDisallowedInPeaceful() when deciding if this mob should despawn.
     */
    @ModifyExpressionValue(method = "checkDespawn", at = @At(value = "INVOKE", target = "Lnet/minecraft/entity/mob/MobEntity;isDisallowedInPeaceful()Z"))
    private boolean bypassPeacefulDespawnCheck(boolean original) {
        return !DoormatSettings.peacefulMonsterSpawning.isEnabled();
    }

}
