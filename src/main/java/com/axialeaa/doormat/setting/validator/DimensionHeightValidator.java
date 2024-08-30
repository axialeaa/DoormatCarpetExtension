package com.axialeaa.doormat.setting.validator;

import carpet.api.settings.CarpetRule;
import carpet.api.settings.Validator;
import net.minecraft.registry.RegistryKey;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.command.ServerCommandSource;
import net.minecraft.world.World;

public abstract class DimensionHeightValidator extends Validator<Integer> {

    public abstract RegistryKey<World> getDimension();

    @Override
    public Integer validate(ServerCommandSource source, CarpetRule<Integer> changingRule, Integer newValue, String userInput) {
        if (source == null)
            return newValue;

        MinecraftServer server = source.getServer();

        if (server.isLoading())
            return newValue;

        World world = server.getWorld(this.getDimension());

        if (world == null)
            return newValue;

        int minRange = Math.min(0, world.getBottomY() + 1);
        int maxRange = Math.max(1, world.getTopY());

        return (newValue >= minRange && newValue <= maxRange) ? newValue : null;
    }

    @Override
    public String description() {
        return "You must choose a value within the %s height limit".formatted(this.getDimension().getValue());
    }

}