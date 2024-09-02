package com.axialeaa.doormat.setting.enum_option.function;

import com.mojang.datafixers.util.Function3;
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

    private final Function3<WorldAccess, BlockPos, SpawnReason, Boolean> function;

    PeacefulMonsterSpawningMode(Function3<WorldAccess, BlockPos, SpawnReason, Boolean> function) {
        this.function = function;
    }

    public boolean canSpawn(WorldAccess world, BlockPos pos, SpawnReason reason) {
        return this.function.apply(world, pos, reason);
    }

    public boolean isEnabled() {
        return this != FALSE;
    }

}