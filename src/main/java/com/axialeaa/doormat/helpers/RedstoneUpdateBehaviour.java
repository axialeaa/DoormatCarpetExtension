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

    /**
     * @return true if the rule is enabled, carpet's quasiConnectivity is set to something above 0 and the block is powered directly or via QC, otherwise true if the block is directly powered.
     */
    public static boolean quasiConnectOn(boolean rule, World world, BlockPos pos) {
        boolean isPowered = world.isReceivingRedstonePower(pos);
        return rule && CarpetSettings.quasiConnectivity != 0 ?
            isPowered || QuasiConnectivity.hasQuasiSignal(world, pos) :
            isPowered;
    }

    /**
     * @return true if the rule flag is 1 or 3.
     * @implNote This is used for disabling certain instances of world.updateNeighbors(), and in principle this returns true for all odd numbers. The only odd numbers present in the flags of the NeighbourUpdateMode enum entries are BLOCK(1) and BOTH(3).
     */
    public static boolean neighborUpdateOn(DoormatSettings.NeighbourUpdateMode rule) {
        return (rule.getFlags() & 1) == 1;
    }

    /**
     * @return if the rule is enabled and the block the torch is resting on is a piston, a value that unpowers the redstone torch otherwise the output of the default redstone power check.
     */
    public static boolean unpowerOnPistonExtended(boolean original, World world, BlockPos pos, Direction direction) {
        BlockPos blockPos = pos.offset(direction);
        BlockState blockState = world.getBlockState(blockPos);
        return DoormatSettings.softInversion && blockState.getBlock() instanceof PistonBlock ?
            blockState.get(PistonBlock.EXTENDED) : original;
    }

}
