package com.axialeaa.doormat.mixin.rules.renewableCobwebs;

import com.axialeaa.doormat.DoormatSettings;
import com.axialeaa.doormat.util.CobwebGeneration;
import com.llamalad7.mixinextras.sugar.Local;
import net.minecraft.block.spawner.MobSpawnerLogic;
import net.minecraft.entity.Entity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.math.BlockPos;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(MobSpawnerLogic.class)
public class MobSpawnerLogicMixin {

    @Shadow private int spawnRange;

    @Inject(method = "serverTick", at = @At(value = "INVOKE", target = "Lnet/minecraft/server/world/ServerWorld;spawnNewEntityAndPassengers(Lnet/minecraft/entity/Entity;)Z"))
    private void generateCobwebsOnSpawnCycle(ServerWorld world, BlockPos pos, CallbackInfo ci, @Local Entity storedEntity) {
        if (DoormatSettings.renewableCobwebs)
            CobwebGeneration.forBox(world, pos, storedEntity, spawnRange, 128);
    }

}