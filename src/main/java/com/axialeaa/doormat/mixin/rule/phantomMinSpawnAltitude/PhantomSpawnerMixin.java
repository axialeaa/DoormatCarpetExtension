package com.axialeaa.doormat.mixin.rule.phantomMinSpawnAltitude;

import com.axialeaa.doormat.DoormatSettings;
import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.world.spawner.PhantomSpawner;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.*;

@Mixin(PhantomSpawner.class)
public class PhantomSpawnerMixin {

    @WrapOperation(method = "spawn", at = @At(value = "INVOKE", target = "Lnet/minecraft/server/world/ServerWorld;getSeaLevel()I"))
    private int modifyAltitude(ServerWorld instance, Operation<Integer> original) {
        return DoormatSettings.phantomMinSpawnAltitude;
    }

}