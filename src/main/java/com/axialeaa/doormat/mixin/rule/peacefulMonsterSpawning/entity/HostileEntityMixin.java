package com.axialeaa.doormat.mixin.rule.peacefulMonsterSpawning.entity;

import com.axialeaa.doormat.setting.DoormatSettings;
import com.llamalad7.mixinextras.injector.ModifyReturnValue;
import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import com.llamalad7.mixinextras.sugar.Local;
import net.minecraft.entity.SpawnReason;
import net.minecraft.entity.mob.HostileEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.Difficulty;
import net.minecraft.world.ServerWorldAccess;
import net.minecraft.world.World;
import net.minecraft.world.WorldAccess;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

@Mixin(HostileEntity.class)
public class HostileEntityMixin {

    @WrapOperation(method = "canSpawnInDark", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/ServerWorldAccess;getDifficulty()Lnet/minecraft/world/Difficulty;"))
    private static Difficulty addSpawnConditionInDark(ServerWorldAccess instance, Operation<Difficulty> original, @Local(argsOnly = true) SpawnReason spawnReason, @Local(argsOnly = true) BlockPos pos) {
        if (DoormatSettings.peacefulMonsterSpawning.canSpawn((World) instance, pos, spawnReason))
            return Difficulty.EASY;

        return original.call(instance);
    }

    @WrapOperation(method = "canSpawnIgnoreLightLevel", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/WorldAccess;getDifficulty()Lnet/minecraft/world/Difficulty;"))
    private static Difficulty addSpawnConditionIgnoreLightLevel(WorldAccess instance, Operation<Difficulty> original, @Local(argsOnly = true) SpawnReason spawnReason, @Local(argsOnly = true) BlockPos pos) {
        if (DoormatSettings.peacefulMonsterSpawning.canSpawn((World) instance, pos, spawnReason))
            return Difficulty.EASY;

        return original.call(instance);
    }

    @ModifyReturnValue(method = "isAngryAt", at = @At("RETURN"))
    private boolean pacifyInPeaceful(boolean original, PlayerEntity player) {
        return DoormatSettings.peacefulMonsterSpawning.isEnabled() ? player.getWorld().getDifficulty() != Difficulty.PEACEFUL : original;
    }

}
