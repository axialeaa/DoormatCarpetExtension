package com.axialeaa.doormat.helpers;

import com.axialeaa.doormat.DoormatSettings;
import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import net.minecraft.entity.SpawnReason;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.Difficulty;
import net.minecraft.world.Heightmap;
import net.minecraft.world.WorldAccess;

public class PeacefulSpawningHelper {

    /**
     * This works because we replace the invocation of world.getDifficulty() (or similar) with null.
     * As a result, the check performed is null != Difficulty.PEACEFUL which always returns true.
     * @implNote In some circumstances, the latter method is used for replacing world.getDifficulty() == Difficulty.PEACEFUL with world.getDifficulty() == null.
     */
    public static Difficulty bypassCheck(WorldAccess world, Operation<Difficulty> original) {
        return DoormatSettings.peacefulMonsterSpawning.enabled() ? null : original.call(world);
    }
    public static Difficulty bypassCheck(Difficulty original) {
        return DoormatSettings.peacefulMonsterSpawning.enabled() ? null : original;
    }

    /**
     * Establishes conditional behaviour based on the selected rule.
     */
    @SuppressWarnings("deprecation")
    public static boolean addSpawningCondition(WorldAccess world, SpawnReason spawnReason, BlockPos pos, boolean original) {
        return original && switch (DoormatSettings.peacefulMonsterSpawning) {
            case BELOW_SURFACE -> pos.getY() < world.getTopPosition(Heightmap.Type.MOTION_BLOCKING_NO_LEAVES, pos).getY();
            case BELOW_SEA -> pos.getY() < world.getSeaLevel();
            case UNNATURAL -> spawnReason != SpawnReason.NATURAL && spawnReason != SpawnReason.CHUNK_GENERATION;
            default -> true;
        };
    }

}
