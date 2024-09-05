package com.axialeaa.doormat.util;

import com.axialeaa.doormat.mixin.rule.comparatorsReadThrough.impl.*;
import net.minecraft.block.BlockState;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.BlockView;

/**
 * Defines a block that comparators can read through, separately from {@link BlockState#isSolidBlock(BlockView, BlockPos)}.
 * @see ComparatorBlockMixin
 * @see WorldMixin
 */
public interface ComparatorSolidBlock {

    /**
     * @return whether comparators can read through this block.
     */
    boolean isValid(BlockState state);

    static boolean isValidInstance(BlockState state) {
        return state.getBlock() instanceof ComparatorSolidBlock solidBlock && solidBlock.isValid(state);
    }

}
