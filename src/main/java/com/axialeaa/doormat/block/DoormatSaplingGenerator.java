package com.axialeaa.doormat.block;

import com.axialeaa.doormat.world.DoormatConfiguredFeatures;
import net.minecraft.block.SaplingGenerator;
import net.minecraft.world.gen.feature.TreeConfiguredFeatures;

import java.util.Optional;

public class DoormatSaplingGenerator {

    public static final SaplingGenerator AZALEA_NO_FLOWERS = new SaplingGenerator("azalea_no_flowers", Optional.empty(), Optional.of(DoormatConfiguredFeatures.AZALEA_TREE_NO_FLOWERS), Optional.empty());
    public static final SaplingGenerator AZALEA_MANY_FLOWERS = new SaplingGenerator("azalea_many_flowers", Optional.empty(), Optional.of(DoormatConfiguredFeatures.AZALEA_TREE_MANY_FLOWERS), Optional.empty());

    public static final SaplingGenerator SWAMP_OAK = new SaplingGenerator("swamp_oak", Optional.empty(), Optional.of(TreeConfiguredFeatures.SWAMP_OAK), Optional.empty());

}
