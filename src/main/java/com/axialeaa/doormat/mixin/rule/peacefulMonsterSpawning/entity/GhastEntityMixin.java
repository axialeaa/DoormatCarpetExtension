package com.axialeaa.doormat.mixin.rule.peacefulMonsterSpawning.entity;

import com.axialeaa.doormat.DoormatSettings;
import com.axialeaa.doormat.helper.PeacefulMonsterSpawningHelper;
import com.llamalad7.mixinextras.injector.ModifyReturnValue;
import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.SpawnReason;
import net.minecraft.entity.mob.GhastEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.random.Random;
import net.minecraft.world.Difficulty;
import net.minecraft.world.WorldAccess;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

@Mixin(GhastEntity.class)
public class GhastEntityMixin {

    @ModifyReturnValue(method = "canSpawn", at = @At("RETURN"))
    private static boolean addSpawnCondition(boolean original, EntityType<GhastEntity> type, WorldAccess world, SpawnReason spawnReason, BlockPos pos, Random random) {
        return original && PeacefulMonsterSpawningHelper.getSpawningCondition(world, pos, spawnReason);
    }

    @WrapOperation(method = "canSpawn", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/WorldAccess;getDifficulty()Lnet/minecraft/world/Difficulty;"))
    private static Difficulty allowPeacefulSpawns(WorldAccess instance, Operation<Difficulty> original) {
        return DoormatSettings.peacefulMonsterSpawning.enabled() ? null : original.call(instance);
    }

}
