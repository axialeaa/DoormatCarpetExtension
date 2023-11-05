package com.axialeaa.doormat.mixin.rules.consistentExplosionImmunity;

import carpet.helpers.OptimizedExplosion;
import carpet.mixins.ExplosionAccessor;
import com.axialeaa.doormat.helpers.ConsistentExplosionImmunity;
import com.llamalad7.mixinextras.injector.WrapWithCondition;
import com.llamalad7.mixinextras.sugar.Local;
import net.minecraft.entity.Entity;
import net.minecraft.entity.damage.DamageSource;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

@Mixin(OptimizedExplosion.class) // optimizedTNT compatibility :D
public class OptimizedExplosionMixin {

    @WrapWithCondition(method = "doExplosionA", at = @At(value = "INVOKE", target = "Lnet/minecraft/entity/Entity;damage(Lnet/minecraft/entity/damage/DamageSource;F)Z"))
    private static boolean disableDamage(Entity entity, DamageSource source, float amount, @Local ExplosionAccessor eAccess) {
        return ConsistentExplosionImmunity.disableDamageIfResistant(entity, eAccess.getRadius());
    }

}
