package com.axialeaa.doormat.helpers;

import com.axialeaa.doormat.DoormatSettings;
import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import net.minecraft.entity.SpawnReason;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.Difficulty;
import net.minecraft.world.Heightmap;
import net.minecraft.world.WorldAccess;

public class PeacefulMonsterSpawning {

    /**
     * This works because we replace the invocation of world.getDifficulty() with the easy difficulty.
     * As a result, the check performed is Difficulty.EASY != Difficulty.PEACEFUL which always returns true.
     */
    public static Difficulty bypassPeacefulCheck(WorldAccess world, Operation<Difficulty> original) {
        return DoormatSettings.peacefulMonsterSpawning.enabled() ? Difficulty.EASY : original.call(world);
    }
    public static Difficulty bypassPeacefulCheck(Difficulty original) {
        return DoormatSettings.peacefulMonsterSpawning.enabled() ? Difficulty.EASY : original;
    }

    /**
     * Establishes conditional behaviour based on the selected rule.
     */
    public static boolean addPeacefulSpawnCondition(WorldAccess world, SpawnReason spawnReason, BlockPos pos, boolean original) {
        return original && switch (DoormatSettings.peacefulMonsterSpawning) {
            case FALSE -> false;
            case TRUE -> true;
            case BELOW_SURFACE -> pos.getY() < world.getTopPosition(Heightmap.Type.MOTION_BLOCKING_NO_LEAVES, pos).getY();
            case BELOW_SEA -> pos.getY() < world.getSeaLevel();
            case UNNATURAL -> spawnReason != SpawnReason.NATURAL && spawnReason != SpawnReason.CHUNK_GENERATION;
        };
    }

}
