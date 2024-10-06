package com.axialeaa.doormat.tinker_kit;

import net.minecraft.block.Block;
import org.jetbrains.annotations.NotNull;

public record Entry<T>(@NotNull TinkerType<?, T> tinkerType, @NotNull T defaultValue) {

    public void put(Block block) {
        this.tinkerType.defaultValues.put(block, this.defaultValue);
    }

}