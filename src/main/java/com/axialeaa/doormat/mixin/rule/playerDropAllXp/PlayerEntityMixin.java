package com.axialeaa.doormat.mixin.rule.playerDropAllXp;

import com.axialeaa.doormat.DoormatSettings;
import net.minecraft.entity.player.PlayerEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(PlayerEntity.class)
public abstract class PlayerEntityMixin {

    @Shadow public int totalExperience;

    @Inject(method = "getXpToDrop", at = @At(value = "INVOKE", target = "Lnet/minecraft/entity/player/PlayerEntity;isSpectator()Z"), cancellable = true)
    private void modifyXpDropped(CallbackInfoReturnable<Integer> cir) {
        if (DoormatSettings.playersDropAllXp)
            cir.setReturnValue(this.totalExperience);
    }

}
