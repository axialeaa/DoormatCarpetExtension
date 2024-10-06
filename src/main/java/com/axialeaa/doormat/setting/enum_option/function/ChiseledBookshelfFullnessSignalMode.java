package com.axialeaa.doormat.setting.enum_option.function;

import net.minecraft.block.entity.ChiseledBookshelfBlockEntity;
import net.minecraft.util.math.MathHelper;

public enum ChiseledBookshelfFullnessSignalMode {

    /**
     * Retains the vanilla behaviour of outputting a signal strength equivalent to the ordinal of the slot previously extracted from or inserted into.
     */
    FALSE (blockEntity -> blockEntity.getLastInteractedSlot() + 1),
    /**
     * Outputs a signal strength equivalent to the number of books in the bookshelf.
     */
    TRUE (ChiseledBookshelfBlockEntity::getFilledSlotCount),
    /**
     * Outputs a signal strength equivalent to the number of books in the bookshelf lerped between 0 and 15; 6 books creates a signal strength of 15.
     */
    LERPED (blockEntity -> {
        int bookCount = TRUE.getOutput(blockEntity);
        int size = blockEntity.size();

        return MathHelper.lerpPositive(bookCount / (float) size, 0, 15);
    });

    private final ChiseledBookshelfFullnessSignalFunction function;

    ChiseledBookshelfFullnessSignalMode(ChiseledBookshelfFullnessSignalFunction function) {
        this.function = function;
    }

    public int getOutput(ChiseledBookshelfBlockEntity blockEntity) {
        return this.function.getOutput(blockEntity);
    }

    @FunctionalInterface
    public interface ChiseledBookshelfFullnessSignalFunction {

        int getOutput(ChiseledBookshelfBlockEntity blockEntity);

    }

}