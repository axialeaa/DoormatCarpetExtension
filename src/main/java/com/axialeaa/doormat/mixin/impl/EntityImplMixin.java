package com.axialeaa.doormat.mixin.impl;

import net.minecraft.entity.Entity;
import net.minecraft.entity.damage.DamageSource;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

/**
 * The purpose behind this mixin is avoiding extending + overriding of the base methods which would be otherwise be incompatible with any mod which tries modifying the same things. Instead, create an empty injection for each method, and extend + override the handler methods from this mixin. That way, method calls are instantiated without clunky instanceof checks and without incompatibility.
 * @implNote <strong>Please avoid using {@link org.spongepowered.asm.mixin.Shadow @Shadow} and {@link org.spongepowered.asm.mixin.Unique @Unique} here. If necessary, cast the mixin instance to the target class instance using {@link Class#cast(Object)}.</strong>
 */
@SuppressWarnings("CancellableInjectionUsage")
@Mixin(Entity.class)
public abstract class EntityImplMixin {

    @Inject(method = "isInvulnerableTo", at = @At("HEAD"), cancellable = true)
    public void isInvulnerableToImpl(DamageSource damageSource, CallbackInfoReturnable<Boolean> cir) {}

    @Inject(method = "isCollidable", at = @At("HEAD"), cancellable = true)
    public void isCollidableImpl(CallbackInfoReturnable<Boolean> cir) {}

}
