package com.axialeaa.doormat.mixin.rule.persistentRespawnAnchorCharge;

import com.axialeaa.doormat.DoormatSettings;
import com.llamalad7.mixinextras.injector.ModifyExpressionValue;
import net.minecraft.entity.player.PlayerEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

@Mixin(PlayerEntity.class)
public class PlayerEntityMixin {

    @ModifyExpressionValue(method = "findRespawnPosition", at = @At(value = "INVOKE", target = "Ljava/util/Optional;isPresent()Z"))
    private static boolean decrementChargeOnRespawn(boolean original) {
        return original && !DoormatSettings.persistentRespawnAnchorCharge;
    }

}
