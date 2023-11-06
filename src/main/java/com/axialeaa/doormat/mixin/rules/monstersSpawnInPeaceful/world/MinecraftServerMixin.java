package com.axialeaa.doormat.mixin.rules.monstersSpawnInPeaceful.world;

import com.axialeaa.doormat.DoormatSettings;
import com.llamalad7.mixinextras.injector.ModifyReturnValue;
import net.minecraft.server.MinecraftServer;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

@Mixin(MinecraftServer.class)
public class MinecraftServerMixin {

    @ModifyReturnValue(method = "isMonsterSpawningEnabled", at = @At("RETURN"))
    private static boolean monsterSpawningEnabledInPeaceful(boolean original) {
        return DoormatSettings.monstersSpawnInPeaceful.isEnabled() || original;
    }

}
