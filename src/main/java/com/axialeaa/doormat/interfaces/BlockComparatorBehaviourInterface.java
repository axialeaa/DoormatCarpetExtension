package com.axialeaa.doormat.interfaces;

import com.axialeaa.doormat.mixin.integrators.ComparatorBlockMixin;
import com.axialeaa.doormat.mixin.integrators.WorldMixin;
import net.minecraft.block.Block;

/**
 * Opt-in interface for easily configurable comparator behaviour via {@link ComparatorBlockMixin} and {@link WorldMixin}.
 */
public interface BlockComparatorBehaviourInterface {

    /**
     * @return whether comparators can read through this block
     */
    boolean canReadThrough(Block block);

}
