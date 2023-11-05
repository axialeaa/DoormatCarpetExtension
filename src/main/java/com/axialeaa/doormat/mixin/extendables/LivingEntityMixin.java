package com.axialeaa.doormat.mixin.extendables;

import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.damage.DamageSource;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

/**
 * The purpose behind this mixin is avoiding extending + overriding of the base methods which would be otherwise be incompatible with any mod which tries modifying the same things.
 * Instead, create an empty injection for each method, and extend + override the handler methods from this mixin.
 * That way, method calls are instantiated without clunky instanceof checks and without incompatibility.
 */

@Mixin(LivingEntity.class)
public abstract class LivingEntityMixin {

    @Inject(method = "damage", at = @At("HEAD"), cancellable = true)
    public void injectedDamage(DamageSource source, float amount, CallbackInfoReturnable<Boolean> cir) {}

}
