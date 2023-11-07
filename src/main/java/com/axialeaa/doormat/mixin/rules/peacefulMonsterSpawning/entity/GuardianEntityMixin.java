package com.axialeaa.doormat.mixin.rules.peacefulMonsterSpawning.entity;

import com.axialeaa.doormat.helpers.PeacefulMonsterSpawning;
import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import net.minecraft.entity.mob.GuardianEntity;
import net.minecraft.world.Difficulty;
import net.minecraft.world.WorldAccess;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

@Mixin(GuardianEntity.class)
public class GuardianEntityMixin {

    @WrapOperation(method = "canSpawn(Lnet/minecraft/entity/EntityType;Lnet/minecraft/world/WorldAccess;Lnet/minecraft/entity/SpawnReason;Lnet/minecraft/util/math/BlockPos;Lnet/minecraft/util/math/random/Random;)Z", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/WorldAccess;getDifficulty()Lnet/minecraft/world/Difficulty;"))
    private static Difficulty allowPeacefulSpawns(WorldAccess world, Operation<Difficulty> original) {
        return PeacefulMonsterSpawning.bypassPeacefulCheck(world, original);
    }

}
