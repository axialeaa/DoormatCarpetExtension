package com.axialeaa.doormat.setting.enum_option.function;

import com.axialeaa.doormat.function.ToBooleanTriFunction;
import net.minecraft.entity.SpawnReason;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.Heightmap;
import net.minecraft.world.WorldAccess;

public enum PeacefulMonsterSpawningMode {

    FALSE         ((world, pos, reason) -> false),
    TRUE          ((world, pos, reason) -> true),
    BELOW_SURFACE ((world, pos, reason) -> pos.getY() < world.getTopPosition(Heightmap.Type.MOTION_BLOCKING_NO_LEAVES, pos).getY()),
    @SuppressWarnings("deprecation")
    BELOW_SEA     ((world, pos, reason) -> pos.getY() < world.getSeaLevel()),
    UNNATURAL     ((world, pos, reason) -> reason != SpawnReason.NATURAL && reason != SpawnReason.CHUNK_GENERATION);

    private final ToBooleanTriFunction<WorldAccess, BlockPos, SpawnReason> function;

    PeacefulMonsterSpawningMode(ToBooleanTriFunction<WorldAccess, BlockPos, SpawnReason> function) {
        this.function = function;
    }

    public boolean canSpawn(WorldAccess world, BlockPos pos, SpawnReason reason) {
        return this.function.applyAsBoolean(world, pos, reason);
    }

    public boolean isEnabled() {
        return this != FALSE;
    }

}