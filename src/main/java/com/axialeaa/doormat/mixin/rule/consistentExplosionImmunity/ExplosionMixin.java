package com.axialeaa.doormat.mixin.rule.consistentExplosionImmunity;

import com.axialeaa.doormat.helper.ConsistentExplosionImmunityHelper;
import com.llamalad7.mixinextras.injector.v2.WrapWithCondition;
import net.minecraft.entity.Entity;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.world.explosion.Explosion;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;

@Mixin(Explosion.class)
public class ExplosionMixin {

    @Shadow @Final private float power;

    @WrapWithCondition(method = "collectBlocksAndDamageEntities", at = @At(value = "INVOKE", target = "Lnet/minecraft/entity/Entity;damage(Lnet/minecraft/entity/damage/DamageSource;F)Z"))
    private boolean shouldDamageEntity(Entity entity, DamageSource source, float amount) {
        return ConsistentExplosionImmunityHelper.shouldDamage(entity, power);
    }

}
