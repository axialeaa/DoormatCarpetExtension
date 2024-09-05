package com.axialeaa.doormat.util;

import com.axialeaa.doormat.mixin.rule.cryingObsidianPortalFrames.impl.*;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;

/**
 * Defines a block that portal frames can be built out of, separately from {@link Blocks#OBSIDIAN}.
 * @see AbstractFireBlockMixin
 * @see NetherPortalMixin
 */
public interface NetherPortalFrameBlock {

    /**
     * @return whether nether portal frames can be built out of this block.
     */
    boolean isValid(BlockState state);

    static boolean isValidInstance(BlockState state) {
        return state.getBlock() instanceof NetherPortalFrameBlock frameBlock && frameBlock.isValid(state);
    }

}
