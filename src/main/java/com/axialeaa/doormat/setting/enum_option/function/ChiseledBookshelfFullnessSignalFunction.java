package com.axialeaa.doormat.setting.enum_option.function;

import net.minecraft.block.entity.ChiseledBookshelfBlockEntity;

@FunctionalInterface
public interface ChiseledBookshelfFullnessSignalFunction {

    int getOutput(ChiseledBookshelfBlockEntity blockEntity);

}