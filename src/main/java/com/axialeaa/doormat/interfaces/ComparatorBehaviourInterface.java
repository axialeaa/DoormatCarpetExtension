package com.axialeaa.doormat.interfaces;

import com.axialeaa.doormat.mixin.integration.ComparatorBlockMixin;
import com.axialeaa.doormat.mixin.integration.WorldMixin;
import net.minecraft.block.Block;

/**
 * Opt-in interface for easily configurable comparator behaviour via {@link ComparatorBlockMixin} and {@link WorldMixin}.
 */
public interface ComparatorBehaviourInterface {

    /**
     * @return whether comparators can read through this block.
     */
    boolean doormat$canReadThrough(Block block);

}
