package com.axialeaa.doormat.mixin.rules.peacefulMonsterSpawning.world;

import com.axialeaa.doormat.DoormatSettings;
import com.llamalad7.mixinextras.injector.ModifyReturnValue;
import net.minecraft.server.MinecraftServer;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

@Mixin(MinecraftServer.class)
public class MinecraftServerMixin {

    /**
     * Satisfies, from what I know, a very small number of back-end checks.
     */
    @ModifyReturnValue(method = "isMonsterSpawningEnabled", at = @At("RETURN"))
    private static boolean monsterSpawningEnabledInPeaceful(boolean original) {
        return original || DoormatSettings.peacefulMonsterSpawning.enabled();
    }

}
