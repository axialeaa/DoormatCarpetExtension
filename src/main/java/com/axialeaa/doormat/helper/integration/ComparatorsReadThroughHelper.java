package com.axialeaa.doormat.helper.integration;

import com.axialeaa.doormat.fake.ComparatorBehaviour;
import net.minecraft.block.BlockState;

public class ComparatorsReadThroughHelper {

    /**
     * @return true if the block to read through is solid or enabled by the interface, or the original solid block check if the block in question does not have the interface implemented.
     */
    public static boolean isEligible(BlockState state) {
        return state.getBlock() instanceof ComparatorBehaviour behaviourInterface && behaviourInterface.doormat$canReadThrough(state);
    }

}
