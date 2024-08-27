package com.axialeaa.doormat.settings.enum_options.function;

import net.minecraft.block.entity.ChiseledBookshelfBlockEntity;
import net.minecraft.util.math.MathHelper;

import java.util.function.ToIntFunction;

public enum ChiseledBookshelfSignalMode {

    FALSE   (blockEntity -> blockEntity.getLastInteractedSlot() + 1),
    TRUE    (ChiseledBookshelfBlockEntity::getFilledSlotCount),
    LERPED  (blockEntity -> MathHelper.lerpPositive(TRUE.getOutput(blockEntity) / (float) blockEntity.size(), 0, 15));

    private final ToIntFunction<ChiseledBookshelfBlockEntity> function;

    ChiseledBookshelfSignalMode(ToIntFunction<ChiseledBookshelfBlockEntity> function) {
        this.function = function;
    }

    public int getOutput(ChiseledBookshelfBlockEntity blockEntity) {
        return this.function.applyAsInt(blockEntity);
    }

}