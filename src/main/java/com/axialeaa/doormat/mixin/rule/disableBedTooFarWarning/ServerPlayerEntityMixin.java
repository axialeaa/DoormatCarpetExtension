package com.axialeaa.doormat.mixin.rule.disableBedTooFarWarning;

import com.axialeaa.doormat.setting.DoormatSettings;
import com.llamalad7.mixinextras.injector.ModifyExpressionValue;
import net.minecraft.server.network.ServerPlayerEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

@Mixin(ServerPlayerEntity.class)
public abstract class ServerPlayerEntityMixin {

    @ModifyExpressionValue(method = "trySleep", at = @At(value = "INVOKE", target = "Lnet/minecraft/server/network/ServerPlayerEntity;isBedWithinRange(Lnet/minecraft/util/math/BlockPos;Lnet/minecraft/util/math/Direction;)Z"))
    private boolean shouldBypassWarning(boolean original) {
        return original || DoormatSettings.disableBedTooFarWarning;
    }

}
