package com.axialeaa.doormat.mixin.rule.peacefulMonsterSpawning.entity;

import com.axialeaa.doormat.helper.PeacefulMonsterSpawningHelper;
import com.axialeaa.doormat.setting.DoormatSettings;
import com.llamalad7.mixinextras.injector.ModifyReturnValue;
import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.SpawnReason;
import net.minecraft.entity.mob.ZombifiedPiglinEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.random.Random;
import net.minecraft.world.Difficulty;
import net.minecraft.world.WorldAccess;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

@Mixin(ZombifiedPiglinEntity.class)
public class ZombifiedPiglinEntityMixin {

    @ModifyReturnValue(method = "canSpawn(Lnet/minecraft/entity/EntityType;Lnet/minecraft/world/WorldAccess;Lnet/minecraft/entity/SpawnReason;Lnet/minecraft/util/math/BlockPos;Lnet/minecraft/util/math/random/Random;)Z", at = @At("RETURN"))
    private static boolean addSpawnCondition(boolean original, EntityType<ZombifiedPiglinEntity> type, WorldAccess world, SpawnReason spawnReason, BlockPos pos, Random random) {
        return original && DoormatSettings.peacefulMonsterSpawning.canSpawn(world, pos, spawnReason);
    }

    @WrapOperation(method = "canSpawn(Lnet/minecraft/entity/EntityType;Lnet/minecraft/world/WorldAccess;Lnet/minecraft/entity/SpawnReason;Lnet/minecraft/util/math/BlockPos;Lnet/minecraft/util/math/random/Random;)Z", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/WorldAccess;getDifficulty()Lnet/minecraft/world/Difficulty;"))
    private static Difficulty allowPeacefulSpawns(WorldAccess instance, Operation<Difficulty> original) {
        return PeacefulMonsterSpawningHelper.bypassCheck(original.call(instance));
    }

}
