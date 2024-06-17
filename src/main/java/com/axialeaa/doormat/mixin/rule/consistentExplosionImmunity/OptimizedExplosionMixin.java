package com.axialeaa.doormat.mixin.rule.consistentExplosionImmunity;

import carpet.helpers.OptimizedExplosion;
import carpet.mixins.ExplosionAccessor;
import com.axialeaa.doormat.helper.ConsistentExplosionImmunityHelper;
import com.llamalad7.mixinextras.injector.v2.WrapWithCondition;
import com.llamalad7.mixinextras.sugar.Local;
import net.minecraft.entity.Entity;
import net.minecraft.entity.ItemEntity;
import net.minecraft.entity.damage.DamageSource;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

@Mixin(OptimizedExplosion.class) // optimizedTNT compatibility :D
public class OptimizedExplosionMixin {

    @WrapWithCondition(method = "doExplosionA", at = @At(value = "INVOKE", target = "Lnet/minecraft/entity/Entity;damage(Lnet/minecraft/entity/damage/DamageSource;F)Z"))
    private static boolean shouldDamageEntity(Entity entity, DamageSource source, float amount, @Local ExplosionAccessor eAccess) {
        if (entity instanceof ItemEntity itemEntity)
            return ConsistentExplosionImmunityHelper.shouldDamage(itemEntity, eAccess.getRadius());

        return true;
    }

}
