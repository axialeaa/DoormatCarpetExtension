package com.axialeaa.doormat.helpers;

import carpet.CarpetSettings;
import carpet.helpers.QuasiConnectivity;
import com.axialeaa.doormat.DoormatSettings;
import net.minecraft.block.BlockState;
import net.minecraft.block.PistonBlock;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.world.World;

public class RedstoneUpdateBehaviour {

    public static boolean quasiConnectOn(boolean rule, World world, BlockPos pos) {
        boolean isPowered = world.isReceivingRedstonePower(pos);
        return rule && CarpetSettings.quasiConnectivity != 0 ?
            QuasiConnectivity.hasQuasiSignal(world, pos) || isPowered :
            isPowered;
    }

    public static boolean neighborUpdateOn(DoormatSettings.NeighbourUpdateMode rule) {
        return rule.getFlags() == 1 || rule.getFlags() == 3;
    }

    public static boolean unpowerOnPistonPowered(boolean original, World world, BlockPos pos, Direction direction) {
        BlockPos blockPos = pos.offset(direction);
        BlockState blockState = world.getBlockState(blockPos);
        if (DoormatSettings.softInversion && blockState.getBlock() instanceof PistonBlock)
            return original || blockState.get(PistonBlock.EXTENDED);
        return original;
    }

}
