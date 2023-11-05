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
            // if the rule is enabled and quasiConnectivity is set to a value above 0...
            isPowered || QuasiConnectivity.hasQuasiSignal(world, pos) :
            // return true if the block is directly powered or receiving a quasi signal
            isPowered; // otherwise return true if the block is directly powered
    }

    public static boolean neighborUpdateOn(DoormatSettings.NeighbourUpdateMode rule) {
        return rule.getFlags() == 1 || rule.getFlags() == 3;
        // used for disabling certain instances of world.updateNeighbors()
    }

    public static boolean unpowerOnPistonExtended(boolean original, World world, BlockPos pos, Direction direction) {
        BlockPos blockPos = pos.offset(direction);
        BlockState blockState = world.getBlockState(blockPos);
        return DoormatSettings.softInversion && blockState.getBlock() instanceof PistonBlock ?
            // if the rule is enabled and the block the torch is placed on is a piston...
            blockState.get(PistonBlock.EXTENDED) : // unpower the torch if the piston extends
            original; // otherwise return the value of the original redstone power check
    }

}
