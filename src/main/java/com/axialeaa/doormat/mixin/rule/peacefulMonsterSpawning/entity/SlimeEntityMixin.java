package com.axialeaa.doormat.mixin.rule.peacefulMonsterSpawning.entity;

import com.axialeaa.doormat.helper.PeacefulMonsterSpawningHelper;
import com.axialeaa.doormat.setting.DoormatSettings;
import com.llamalad7.mixinextras.injector.ModifyExpressionValue;
import com.llamalad7.mixinextras.injector.ModifyReturnValue;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.SpawnReason;
import net.minecraft.entity.mob.SlimeEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.random.Random;
import net.minecraft.world.Difficulty;
import net.minecraft.world.WorldAccess;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

@Mixin(SlimeEntity.class)
public class SlimeEntityMixin {

    @ModifyReturnValue(method = "canSpawn", at = {@At(value = "RETURN", ordinal = 0), @At(value = "RETURN", ordinal = 2)})
    private static boolean addSpawnCondition(boolean original, EntityType<SlimeEntity> type, WorldAccess world, SpawnReason spawnReason, BlockPos pos, Random random) {
        return original && DoormatSettings.peacefulMonsterSpawning.canSpawn(world, pos, spawnReason);
    }

    @ModifyExpressionValue(method = "canSpawn", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/WorldAccess;getDifficulty()Lnet/minecraft/world/Difficulty;"))
    private static Difficulty bypassPeacefulConversionCheck(Difficulty original) {
        return PeacefulMonsterSpawningHelper.bypassCheck(original);
    }

}
