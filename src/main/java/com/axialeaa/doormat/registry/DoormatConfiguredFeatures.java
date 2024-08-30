package com.axialeaa.doormat.registry;

import com.axialeaa.doormat.Doormat;
import net.minecraft.registry.RegistryKey;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.world.gen.feature.ConfiguredFeature;

public class DoormatConfiguredFeatures {

    public static final RegistryKey<ConfiguredFeature<?, ?>> AZALEA_TREE_NO_FLOWERS = register("azalea_tree_no_flowers");
    public static final RegistryKey<ConfiguredFeature<?, ?>> AZALEA_TREE_MANY_FLOWERS = register("azalea_tree_many_flowers");

    public static final RegistryKey<ConfiguredFeature<?, ?>> MOSSY_COBBLESTONE_PATCH = register("mossy_cobblestone_patch");
    public static final RegistryKey<ConfiguredFeature<?, ?>> MOSSY_STONE_BRICKS_PATCH = register("mossy_stone_bricks_patch");

    public static RegistryKey<ConfiguredFeature<?,?>> register(String name) {
        return RegistryKey.of(RegistryKeys.CONFIGURED_FEATURE, Doormat.id(name));
    }

}