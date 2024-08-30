package com.axialeaa.doormat.setting.validator;

import net.minecraft.registry.RegistryKey;
import net.minecraft.world.World;

public class OverworldHeightValidator extends DimensionHeightValidator {

    @Override
    public RegistryKey<World> getDimension() {
        return World.OVERWORLD;
    }

}