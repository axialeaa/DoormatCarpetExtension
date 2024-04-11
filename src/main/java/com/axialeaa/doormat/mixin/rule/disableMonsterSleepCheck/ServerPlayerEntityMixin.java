package com.axialeaa.doormat.mixin.rule.disableMonsterSleepCheck;

import com.axialeaa.doormat.DoormatSettings;
import com.llamalad7.mixinextras.injector.ModifyExpressionValue;
import net.minecraft.server.network.ServerPlayerEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

@Mixin(ServerPlayerEntity.class)
public class ServerPlayerEntityMixin {

    @ModifyExpressionValue(method = "trySleep", at = @At(value = "INVOKE", target = "Lnet/minecraft/server/network/ServerPlayerEntity;isCreative()Z"))
    private boolean shouldBypassMonsterCheck(boolean original) {
        return original || DoormatSettings.disableMonsterSleepCheck;
    }

}
