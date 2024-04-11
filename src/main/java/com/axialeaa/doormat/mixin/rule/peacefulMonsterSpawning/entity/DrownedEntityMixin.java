package com.axialeaa.doormat.mixin.rule.peacefulMonsterSpawning.entity;

import com.axialeaa.doormat.helper.PeacefulMonsterSpawningHelper;
import com.llamalad7.mixinextras.injector.ModifyReturnValue;
import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.SpawnReason;
import net.minecraft.entity.mob.DrownedEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.random.Random;
import net.minecraft.world.Difficulty;
import net.minecraft.world.ServerWorldAccess;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Slice;

@Mixin(DrownedEntity.class)
public class DrownedEntityMixin {

    @ModifyReturnValue(method = "canSpawn(Lnet/minecraft/entity/EntityType;Lnet/minecraft/world/ServerWorldAccess;Lnet/minecraft/entity/SpawnReason;Lnet/minecraft/util/math/BlockPos;Lnet/minecraft/util/math/random/Random;)Z", slice = @Slice(from = @At(value = "INVOKE", target = "Lnet/minecraft/registry/entry/RegistryEntry;isIn(Lnet/minecraft/registry/tag/TagKey;)Z")), at = @At("RETURN"))
    private static boolean addSpawnCondition(boolean original, EntityType<DrownedEntity> type, ServerWorldAccess world, SpawnReason spawnReason, BlockPos pos, Random random) {
        return original && PeacefulMonsterSpawningHelper.getSpawningCondition(world, spawnReason, pos);
    }

    @WrapOperation(method = "canSpawn(Lnet/minecraft/entity/EntityType;Lnet/minecraft/world/ServerWorldAccess;Lnet/minecraft/entity/SpawnReason;Lnet/minecraft/util/math/BlockPos;Lnet/minecraft/util/math/random/Random;)Z", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/ServerWorldAccess;getDifficulty()Lnet/minecraft/world/Difficulty;"))
    private static Difficulty allowPeacefulSpawns(ServerWorldAccess instance, Operation<Difficulty> original) {
        return PeacefulMonsterSpawningHelper.bypassCheck(original.call(instance));
    }

}