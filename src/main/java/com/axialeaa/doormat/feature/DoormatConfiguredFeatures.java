package com.axialeaa.doormat.feature;

import com.axialeaa.doormat.DoormatServer;
import net.minecraft.registry.RegistryKey;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.util.Identifier;
import net.minecraft.world.gen.feature.ConfiguredFeature;

public class DoormatConfiguredFeatures {

    public static final RegistryKey<ConfiguredFeature<?, ?>> AZALEA_TREE_NO_FLOWERS = registerKey("azalea_tree_no_flowers");
    public static final RegistryKey<ConfiguredFeature<?, ?>> AZALEA_TREE_MANY_FLOWERS = registerKey("azalea_tree_many_flowers");

    public static final RegistryKey<ConfiguredFeature<?, ?>> MOSSY_COBBLESTONE_PATCH = registerKey("mossy_cobblestone_patch");
    public static final RegistryKey<ConfiguredFeature<?, ?>> MOSSY_STONE_BRICKS_PATCH = registerKey("mossy_stone_bricks_patch");

    public static RegistryKey<ConfiguredFeature<?,?>> registerKey(String name) {
        return RegistryKey.of(RegistryKeys.CONFIGURED_FEATURE, new Identifier(DoormatServer.MOD_ID, name));
    }

}