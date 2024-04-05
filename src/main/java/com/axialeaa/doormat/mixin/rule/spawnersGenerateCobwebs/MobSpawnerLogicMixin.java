package com.axialeaa.doormat.mixin.rule.spawnersGenerateCobwebs;

import com.axialeaa.doormat.helper.rule.CobwebGenerationHelper;
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
        CobwebGenerationHelper.forBox(world, pos, storedEntity, spawnRange, 128);
    }

}