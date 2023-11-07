package com.axialeaa.doormat.mixin.rules.peacefulMonsterSpawning.entity;

import com.axialeaa.doormat.helpers.PeacefulMonsterSpawning;
import com.llamalad7.mixinextras.injector.ModifyReturnValue;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.SpawnReason;
import net.minecraft.entity.mob.MagmaCubeEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.random.Random;
import net.minecraft.world.WorldAccess;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

@Mixin(MagmaCubeEntity.class)
public class MagmaCubeEntityMixin {

    @ModifyReturnValue(method = "canMagmaCubeSpawn", at = @At("RETURN"))
    private static boolean addSpawnCondition(boolean original, EntityType<MagmaCubeEntity> type, WorldAccess world, SpawnReason spawnReason, BlockPos pos, Random random) {
        return PeacefulMonsterSpawning.addPeacefulSpawnCondition(world, spawnReason, pos, original);
    }

}
