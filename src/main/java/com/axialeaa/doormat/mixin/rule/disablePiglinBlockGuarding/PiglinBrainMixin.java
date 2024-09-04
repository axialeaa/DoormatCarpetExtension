package com.axialeaa.doormat.mixin.rule.disablePiglinBlockGuarding;

import com.axialeaa.doormat.setting.DoormatSettings;
import net.minecraft.entity.mob.PiglinBrain;
import net.minecraft.entity.player.PlayerEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(PiglinBrain.class)
public class PiglinBrainMixin {

    @Inject(method = "onGuardedBlockInteracted", at = @At("HEAD"), cancellable = true)
    private static void cancelAttack(PlayerEntity player, boolean blockOpen, CallbackInfo ci) {
        if (DoormatSettings.disablePiglinBlockGuarding.shouldNegateAnger(player, blockOpen))
            ci.cancel();
    }

}
