package com.axialeaa.doormat.mixin;

import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.damage.DamageSource;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(LivingEntity.class)
public abstract class LivingEntityMixin {

    // the purpose behind this mixin is avoiding extending + overriding of the base methods which would be incompatible
    //      with any mod that wants to modify the same things
    // instead, create an empty injection for each method, and extend + override the handler methods from this mixin
    // that way, method calls are instantiated without clunky instanceof checks and without incompatibility

    @Inject(method = "damage", at = @At("HEAD"), cancellable = true)
    public void injectedDamage(DamageSource source, float amount, CallbackInfoReturnable<Boolean> cir) {}

}
