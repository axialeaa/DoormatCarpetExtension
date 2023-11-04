package com.axialeaa.doormat.fakes;

import com.axialeaa.doormat.mixin.rule.comparatorsReadThrough.*;
import net.minecraft.block.Block;

/**
 * opt-in interface for easily configurable comparator behaviour via {@link ComparatorBlockMixin} and {@link WorldMixin}
 */
public interface BlockComparatorBehaviourInterface {

    /**
     * @return whether comparators can read through this block
     */
    boolean canReadThrough(Block block);

}
