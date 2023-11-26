package com.axialeaa.doormat.mixin.rules.compactEnchantTooltips;

import com.axialeaa.doormat.helpers.EnchantmentCarousel;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.world.ClientWorld;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Environment(EnvType.CLIENT)
@Mixin(MinecraftClient.class)
public abstract class MinecraftClientMixin {

    @Shadow @Nullable public ClientWorld world;

    @Inject(method = "tick", at = @At("HEAD"))
    private void getCarouselTime(CallbackInfo ci) {
        if (this.world != null)
            EnchantmentCarousel.tick(this.world.getTime());
    }

}
