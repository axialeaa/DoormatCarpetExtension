package com.axialeaa.doormat.registry;

import com.google.common.collect.ImmutableMap;
import net.minecraft.block.Block;
import net.minecraft.block.Blocks;
import net.minecraft.block.SaplingGenerator;
import net.minecraft.registry.RegistryKey;
import net.minecraft.world.gen.feature.ConfiguredFeature;
import net.minecraft.world.gen.feature.TreeConfiguredFeatures;

import java.util.Map;
import java.util.Optional;

public class DoormatSaplingGenerators {

    public static final SaplingGenerator AZALEA_NO_FLOWERS = create("azalea_no_flowers", DoormatConfiguredFeatures.AZALEA_TREE_NO_FLOWERS);
    public static final SaplingGenerator AZALEA_MANY_FLOWERS = create("azalea_many_flowers", DoormatConfiguredFeatures.AZALEA_TREE_MANY_FLOWERS);

    public static final SaplingGenerator SWAMP_OAK = create("swamp_oak", TreeConfiguredFeatures.SWAMP_OAK);

    private static SaplingGenerator create(String name, RegistryKey<ConfiguredFeature<?, ?>> key) {
        return new SaplingGenerator(name, Optional.empty(), Optional.of(key), Optional.empty());
    }

    public static final Map<Block, SaplingGenerator> AZALEA_MAP = ImmutableMap.of(
        Blocks.AZALEA, DoormatSaplingGenerators.AZALEA_NO_FLOWERS,
        Blocks.FLOWERING_AZALEA, DoormatSaplingGenerators.AZALEA_MANY_FLOWERS
    );

}