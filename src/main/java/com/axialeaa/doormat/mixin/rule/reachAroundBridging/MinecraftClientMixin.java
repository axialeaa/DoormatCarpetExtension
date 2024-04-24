package com.axialeaa.doormat.mixin.rule.reachAroundBridging;

import com.axialeaa.doormat.helper.ReachAroundPlacementHelper;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.client.world.ClientWorld;
import net.minecraft.util.hit.HitResult;
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
    @Shadow @Nullable public ClientPlayerEntity player;
    @Shadow @Nullable public HitResult crosshairTarget;

    /**
     * If the rule is enabled, allows players to place blocks one block in front of the one they're standing on.
     * @implNote This works by checking if the line between the player and the end of their reach distance intersects with the blockpos of the standing position, offset by 1 in the facing direction. Thanks again for the help, <a href="https://github.com/lntricate1">intricate</a>!
     */
    @Inject(method = "doItemUse", at = @At(value = "INVOKE", target = "Lnet/minecraft/util/hit/HitResult;getType()Lnet/minecraft/util/hit/HitResult$Type;", shift = At.Shift.BEFORE))
    private void setCrosshairTarget(CallbackInfo ci) {
        crosshairTarget = ReachAroundPlacementHelper.getHitResult(world, player, crosshairTarget);
    }

}