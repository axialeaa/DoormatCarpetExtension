package com.axialeaa.doormat.mixin.impl;

import net.minecraft.block.entity.BlockEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

/**
 * The purpose behind this mixin is avoiding extending + overriding of the base methods which would be otherwise be incompatible with any mod which tries modifying the same things. Instead, create an empty injection for each method, and extend + override the handler methods from this mixin. That way, method calls are instantiated without clunky instanceof checks and without incompatibility.
 * @implNote <strong>Please avoid using {@link org.spongepowered.asm.mixin.Shadow @Shadow} and {@link org.spongepowered.asm.mixin.Unique @Unique} here. If necessary, cast the mixin instance to the target class instance using {@link Class#cast(Object)}.</strong>
 */
@Mixin(BlockEntity.class)
public abstract class BlockEntityImplMixin {

    @Inject(method = "markDirty()V", at = @At("HEAD"))
    public void markDirtyImpl(CallbackInfo ci) {}

}
