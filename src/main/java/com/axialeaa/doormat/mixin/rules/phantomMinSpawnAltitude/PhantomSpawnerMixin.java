package com.axialeaa.doormat.mixin.rules.phantomMinSpawnAltitude;

import com.axialeaa.doormat.DoormatSettings;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.world.spawner.PhantomSpawner;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.*;

@Mixin(PhantomSpawner.class)
public class PhantomSpawnerMixin {

    @Redirect(method = "spawn", at = @At(value = "INVOKE", target = "Lnet/minecraft/server/world/ServerWorld;getSeaLevel()I"))
    private int modifyAltitude(ServerWorld world) {
        return DoormatSettings.phantomMinSpawnAltitude;
    }

}