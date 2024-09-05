package com.axialeaa.doormat.setting.enum_option.function;

import com.axialeaa.doormat.util.function.ToBooleanTriFunction;
import net.minecraft.entity.SpawnReason;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.Heightmap;
import net.minecraft.world.World;

/**
 * @see <a href="https://github.com/axialeaa/DoormatCarpetExtension/wiki/Using-peacefulMonsterSpawning">Using peacefulMonsterSpawning</a>
 */
public enum PeacefulMonsterSpawningMode {

    /**
     * Retains vanilla peaceful mode behaviour.
     */
    FALSE (false),
    /**
     * Spawns the entity the same as it would spawn in {@link net.minecraft.world.Difficulty#EASY Difficulty.EASY}.
     */
    TRUE (true),
    /**
     * Spawns the entity if the spawn position is anywhere below the maximum y level as read by the {@link Heightmap.Type#MOTION_BLOCKING_NO_LEAVES} heightmap.
     * @see <a href="https://github.com/axialeaa/DoormatCarpetExtension/wiki/Using-peacefulMonsterSpawning#below_surface-and-heightmaps">Using peacefulMonsterSpawning§Below Surface and Heightmaps</a>
     */
    BELOW_SURFACE ((world, pos, reason) -> {
        Heightmap.Type heightmap = Heightmap.Type.MOTION_BLOCKING_NO_LEAVES;
        BlockPos topPos = world.getTopPosition(heightmap, pos);

        return pos.getY() < topPos.getY();
    }),
    /**
     * Spawns the entity if the spawn position is anywhere below the sea level.
     * @see <a href="https://github.com/axialeaa/DoormatCarpetExtension/wiki/Using-peacefulMonsterSpawning#below_sea-and-the-sea-level">Using peacefulMonsterSpawning§Below Sea and the Sea Level</a>
     */
    BELOW_SEA ((world, pos, reason) -> pos.getY() < world.getSeaLevel()),
    /**
     * Spawns the entity if the {@link SpawnReason} is anything other than {@link SpawnReason#NATURAL} or {@link SpawnReason#CHUNK_GENERATION}.
     * @see <a href="https://github.com/axialeaa/DoormatCarpetExtension/wiki/Using-peacefulMonsterSpawning#unnatural-and-spawn-types">Using peacefulMonsterSpawning§Unnatural and Spawn Types</a>
     */
    UNNATURAL ((world, pos, reason) -> {
        boolean natural = reason == SpawnReason.NATURAL;
        boolean chunkGen = reason == SpawnReason.CHUNK_GENERATION;

        return !natural && !chunkGen;
    });

    private final ToBooleanTriFunction<World, BlockPos, SpawnReason> function;

    PeacefulMonsterSpawningMode(boolean canSpawn) {
        this.function = (world, pos, reason) -> canSpawn;
    }

    PeacefulMonsterSpawningMode(ToBooleanTriFunction<World, BlockPos, SpawnReason> function) {
        this.function = function;
    }

    public boolean canSpawn(World world, BlockPos pos, SpawnReason reason) {
        return this.function.apply(world, pos, reason);
    }

    public boolean isEnabled() {
        return this != FALSE;
    }

}