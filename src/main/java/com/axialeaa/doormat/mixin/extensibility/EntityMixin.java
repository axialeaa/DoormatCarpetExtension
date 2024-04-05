package com.axialeaa.doormat.mixin.extensibility;

import net.minecraft.entity.Entity;
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
@SuppressWarnings("CancellableInjectionUsage")
@Mixin(Entity.class)
public abstract class EntityMixin {

    @Inject(method = "damage", at = @At("HEAD"), cancellable = true)
    public void damageImpl(DamageSource source, float amount, CallbackInfoReturnable<Boolean> cir) {}

    @Inject(method = "isCollidable", at = @At("HEAD"), cancellable = true)
    public void isCollidableImpl(CallbackInfoReturnable<Boolean> cir) {}

}
