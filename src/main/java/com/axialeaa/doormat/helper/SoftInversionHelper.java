package com.axialeaa.doormat.helper;

import com.axialeaa.doormat.setting.DoormatSettings;
import net.minecraft.block.BlockState;
import net.minecraft.block.PistonBlock;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class SoftInversionHelper {

    public static boolean isPistonExtended(World world, BlockPos pos) {
        if (!DoormatSettings.softInversion)
            return false;

        BlockState blockState = world.getBlockState(pos);

        return blockState.getBlock() instanceof PistonBlock && blockState.get(PistonBlock.EXTENDED);
    }

}
