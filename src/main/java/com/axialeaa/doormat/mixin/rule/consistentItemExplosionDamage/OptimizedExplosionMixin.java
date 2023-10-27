package com.axialeaa.doormat.mixin.rule.consistentItemExplosionDamage;

import carpet.helpers.OptimizedExplosion;
import carpet.mixins.ExplosionAccessor;
import com.axialeaa.doormat.DoormatSettings;
import com.axialeaa.doormat.helpers.ConsistentItemExplosionDamage;
import com.llamalad7.mixinextras.injector.WrapWithCondition;
import com.llamalad7.mixinextras.sugar.Local;
import net.minecraft.entity.Entity;
import net.minecraft.entity.damage.DamageSource;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

@Mixin(OptimizedExplosion.class)
public class OptimizedExplosionMixin {

    @SuppressWarnings("unused")
    @WrapWithCondition(method = "doExplosionA", at = @At(value = "INVOKE", target = "Lnet/minecraft/entity/Entity;damage(Lnet/minecraft/entity/damage/DamageSource;F)Z"))
    private static boolean disableDamage(Entity entity, DamageSource source, float amount, @Local ExplosionAccessor eAccess) {
        return ConsistentItemExplosionDamage.disableDamageIfResistant(DoormatSettings.consistentItemExplosionDamage, entity, eAccess.getRadius());
    }

}
