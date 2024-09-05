package com.axialeaa.doormat.mixin.rule.peacefulMonsterSpawning.entity.effect;

import com.axialeaa.doormat.setting.DoormatSettings;
import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.world.Difficulty;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

@Mixin(targets = "net.minecraft.entity.effect.BadOmenStatusEffect")
public class BadOmenStatusEffectMixin {

    @WrapOperation(method = "applyUpdateEffect", at = @At(value = "INVOKE", target = "Lnet/minecraft/server/world/ServerWorld;getDifficulty()Lnet/minecraft/world/Difficulty;"))
    private Difficulty allowPeacefulEffect(ServerWorld instance, Operation<Difficulty> original) {
        return DoormatSettings.peacefulMonsterSpawning.isEnabled() ? null : original.call(instance);
    }

}
