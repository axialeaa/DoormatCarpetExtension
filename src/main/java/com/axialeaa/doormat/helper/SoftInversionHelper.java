package com.axialeaa.doormat.helper;

import net.minecraft.block.BlockState;
import net.minecraft.block.PistonBlock;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class SoftInversionHelper {

    public static boolean isPistonExtended(World world, BlockPos pos) {
        BlockState blockState = world.getBlockState(pos);
        return blockState.getBlock() instanceof PistonBlock && blockState.get(PistonBlock.EXTENDED);
    }

}
