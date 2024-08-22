package com.axialeaa.doormat.fake;

import com.axialeaa.doormat.mixin.rule.comparatorsReadThrough.functionality.ComparatorBlockMixin;
import com.axialeaa.doormat.mixin.rule.comparatorsReadThrough.functionality.WorldMixin;
import net.minecraft.block.BlockState;

/**
 * Opt-in interface for easily configurable comparator behaviour via {@link ComparatorBlockMixin} and {@link WorldMixin}.
 */
public interface ComparatorBehaviour {

    /**
     * @return whether comparators can read through this block.
     */
    boolean canReadThrough(BlockState state);

}
