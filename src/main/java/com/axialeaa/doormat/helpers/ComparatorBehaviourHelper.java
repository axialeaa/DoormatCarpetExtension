package com.axialeaa.doormat.helpers;

import com.axialeaa.doormat.interfaces.ComparatorBehaviourInterface;
import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import net.minecraft.block.BlockState;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.BlockView;

public class ComparatorBehaviourHelper {

    public static boolean modifyBlockCheck(BlockState state, BlockView world, BlockPos pos, Operation<Boolean> original) {
        return original.call(state, world, pos) || state.getBlock() instanceof ComparatorBehaviourInterface BCBI && BCBI.doormat$canReadThrough(state.getBlock());
    }

}
