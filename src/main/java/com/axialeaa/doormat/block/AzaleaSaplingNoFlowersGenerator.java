package com.axialeaa.doormat.block;

import com.axialeaa.doormat.world.DoormatConfiguredFeatures;
import net.minecraft.block.sapling.SaplingGenerator;
import net.minecraft.registry.RegistryKey;
import net.minecraft.util.math.random.Random;
import net.minecraft.world.gen.feature.ConfiguredFeature;

public class AzaleaSaplingNoFlowersGenerator extends SaplingGenerator {

    public AzaleaSaplingNoFlowersGenerator() {}

    protected RegistryKey<ConfiguredFeature<?, ?>> getTreeFeature(Random random, boolean bees) {
        return DoormatConfiguredFeatures.AZALEA_TREE_NO_FLOWERS;
    }

}
