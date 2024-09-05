package com.axialeaa.doormat.util;

import com.axialeaa.doormat.mixin.rule.dispensersBuryItems.BrushableBlockEntityMixin;
import net.minecraft.block.entity.BrushableBlockEntity;
import net.minecraft.item.ItemStack;

/**
 * Implements a single method which can be used to set the stored item stack of a {@link BrushableBlockEntity} instance without using access wideners.
 * @see BrushableBlockEntityMixin
 */
public interface SingleStackSetter {

    void setStack(ItemStack stack);

}