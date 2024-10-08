package com.axialeaa.doormat.helper;

import com.google.common.collect.ImmutableMap;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.SpawnGroup;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.biome.SpawnSettings;
import java.util.List;
import java.util.Map;
import java.util.Optional;

public class DungeonBiomeSpawningHelper {

    private static final Map<EntityType<?>, List<EntityType<?>>> MAP = ImmutableMap.of(
        EntityType.ZOMBIE, List.of(EntityType.HUSK),
        EntityType.SKELETON, List.of(EntityType.STRAY, EntityType.BOGGED)
    );

    private static boolean canSpawnInBiome(Biome biome, EntityType<?> type) {
        SpawnSettings settings = biome.getSpawnSettings();
        SpawnGroup spawnGroup = type.getSpawnGroup();

        List<SpawnSettings.SpawnEntry> spawnEntries = settings.getSpawnEntries(spawnGroup).getEntries();

        for (SpawnSettings.SpawnEntry spawnEntry : spawnEntries) {
            if (spawnEntry.type == type)
                return true;
        }

        return false;
    }

    public static Optional<EntityType<?>> getAlternativeForBiome(EntityType<?> type, Biome biome) {
        if (!MAP.containsKey(type))
            return Optional.empty();

        for (EntityType<?> entityType : MAP.get(type)) {
            if (canSpawnInBiome(biome, entityType))
                return Optional.of(entityType);
        }

        return Optional.empty();
    }

}
