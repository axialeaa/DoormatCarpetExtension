package com.axialeaa.doormat.fake;

import net.minecraft.block.BlockState;

/**
 * Opt-in interface for easily configurable comparator behaviour via {@link com.axialeaa.doormat.mixin.integration.ComparatorBlockMixin ComparatorBlockMixin} and {@link com.axialeaa.doormat.mixin.integration.WorldMixin WorldMixin}.
 */
@FunctionalInterface
public interface ComparatorBehaviour {

    /**
     * @return whether comparators can read through this block.
     */
    boolean canReadThrough(BlockState state);

}
