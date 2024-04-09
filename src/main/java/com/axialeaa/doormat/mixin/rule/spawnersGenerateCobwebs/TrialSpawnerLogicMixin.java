package com.axialeaa.doormat.mixin.rule.spawnersGenerateCobwebs;

import com.axialeaa.doormat.helper.CobwebGenerationHelper;
import com.llamalad7.mixinextras.sugar.Local;
import net.minecraft.block.spawner.TrialSpawnerLogic;
import net.minecraft.entity.Entity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.math.BlockPos;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.Optional;
import java.util.UUID;

@Mixin(TrialSpawnerLogic.class)
public class TrialSpawnerLogicMixin {

    @Inject(method = "trySpawnMob", at = @At(value = "INVOKE", target = "Lnet/minecraft/server/world/ServerWorld;spawnNewEntityAndPassengers(Lnet/minecraft/entity/Entity;)Z"))
    private void generateCobwebsOnSpawnCycle(ServerWorld world, BlockPos pos, CallbackInfoReturnable<Optional<UUID>> cir, @Local Entity storedEntity) {
        CobwebGenerationHelper.forBox(world, pos, storedEntity, 1, 0, 16);
    }

}
