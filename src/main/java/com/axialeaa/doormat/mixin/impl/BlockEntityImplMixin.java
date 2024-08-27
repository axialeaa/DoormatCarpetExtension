package com.axialeaa.doormat.mixin.impl;

import net.minecraft.block.BlockState;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

/**
 * The purpose behind this mixin is avoiding extending + overriding of the base methods which would be otherwise be incompatible with any mod which tries modifying the same things.
 * Instead, create an empty injection for each method, and extend + override the handler methods from this mixin.
 * That way, method calls are instantiated without clunky instanceof checks and without incompatibility.
 */
@Mixin(BlockEntity.class)
public abstract class BlockEntityImplMixin {

    @Shadow public abstract BlockState getCachedState();

    @Shadow public abstract BlockPos getPos();

    @Shadow @Nullable public abstract World getWorld();

    @Inject(method = "markDirty()V", at = @At("HEAD"))
    public void markDirtyImpl(CallbackInfo ci) {}

}
