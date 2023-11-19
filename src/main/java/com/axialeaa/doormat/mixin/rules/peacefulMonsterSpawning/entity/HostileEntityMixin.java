package com.axialeaa.doormat.mixin.rules.peacefulMonsterSpawning.entity;

import com.axialeaa.doormat.DoormatSettings;
import com.axialeaa.doormat.helpers.PeacefulMonsterSpawning;
import com.llamalad7.mixinextras.injector.ModifyReturnValue;
import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.SpawnReason;
import net.minecraft.entity.mob.HostileEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.random.Random;
import net.minecraft.world.Difficulty;
import net.minecraft.world.ServerWorldAccess;
import net.minecraft.world.WorldAccess;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

@Mixin(HostileEntity.class)
public class HostileEntityMixin {

    /**
     * <h2>SPAWN CONDITION ADDERS</h2>
     * @return the normal return values of these methods with an additional check based on the rule.
     */
    @ModifyReturnValue(method = "canSpawnInDark", at = @At("RETURN"))
    private static boolean addSpawnConditionInDark(boolean original, EntityType<? extends HostileEntity> type, ServerWorldAccess world, SpawnReason spawnReason, BlockPos pos, Random random) {
        return PeacefulMonsterSpawning.addPeacefulSpawnCondition(world, spawnReason, pos, original);
    }
    @ModifyReturnValue(method = "canSpawnIgnoreLightLevel", at = @At("RETURN"))
    private static boolean addSpawnConditionIgnoreLightLevel(boolean original, EntityType<? extends HostileEntity> type, WorldAccess world, SpawnReason spawnReason, BlockPos pos, Random random) {
        return PeacefulMonsterSpawning.addPeacefulSpawnCondition(world, spawnReason, pos, original);
    }

    /**
     * <h2>CHECK BYPASSES</h2>
     * @return the normal return values of these methods without the Difficulty.PEACEFUL check.
     */
    @WrapOperation(method = "canSpawnInDark", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/ServerWorldAccess;getDifficulty()Lnet/minecraft/world/Difficulty;"))
    private static Difficulty allowPeacefulSpawnsInDark(ServerWorldAccess world, Operation<Difficulty> original) {
        return PeacefulMonsterSpawning.bypassPeacefulCheck(world, original);
    }
    @WrapOperation(method = "canSpawnIgnoreLightLevel", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/WorldAccess;getDifficulty()Lnet/minecraft/world/Difficulty;"))
    private static Difficulty allowPeacefulSpawnsIgnoreLightLevel(WorldAccess world, Operation<Difficulty> original) {
        return PeacefulMonsterSpawning.bypassPeacefulCheck(world, original);
    }

    /**
     * Disables mob anger in peaceful mode. This may not be necessary, but it covers our bases in case it is.
     */
    @ModifyReturnValue(method = "isAngryAt", at = @At("RETURN"))
    private boolean pacifyInPeaceful(boolean original, PlayerEntity player) {
        return DoormatSettings.peacefulMonsterSpawning.enabled() ? player.getWorld().getDifficulty() != Difficulty.PEACEFUL : original;
    }

}
