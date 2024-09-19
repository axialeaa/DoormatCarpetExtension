package com.axialeaa.doormat.tinker_kit;

import net.minecraft.block.Block;

/**
 * This is where the types of updates each redstone component can emit are defined.
 */
public enum UpdateType {

    NEITHER(0),
    BLOCK(Block.NOTIFY_NEIGHBORS),
    SHAPE(Block.NOTIFY_LISTENERS),
    BOTH(Block.NOTIFY_ALL);

    public final int flags;

    UpdateType(int flags) {
        this.flags = flags;
    }

}