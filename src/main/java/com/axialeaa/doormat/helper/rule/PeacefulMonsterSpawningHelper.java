package com.axialeaa.doormat.helper.rule;

import com.axialeaa.doormat.DoormatSettings;
import net.minecraft.entity.SpawnReason;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.Difficulty;
import net.minecraft.world.Heightmap;
import net.minecraft.world.WorldAccess;

public class PeacefulMonsterSpawningHelper {

    /**
     * This works because we replace the invocation of world.getDifficulty() (or similar) with null.
     * As a result, the check performed is null != Difficulty.PEACEFUL which always returns true.
     * @implNote In some circumstances, this method is used for replacing world.getDifficulty() == Difficulty.PEACEFUL with world.getDifficulty() == null.
     */
    public static Difficulty bypassCheck(Difficulty difficulty) {
        return DoormatSettings.peacefulMonsterSpawning.enabled() ? null : difficulty;
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
