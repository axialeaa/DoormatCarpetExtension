package com.axialeaa.doormat.helpers;

import carpet.CarpetSettings;
import carpet.helpers.QuasiConnectivity;
import com.axialeaa.doormat.DoormatSettings;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class RedstoneHelper {

    /**
     * @return true if the rule is enabled, carpet's quasiConnectivity is set to something above 0 and the block is powered directly or via QC, otherwise true if the block is directly powered.
     */
    public static boolean quasiConnectForRule(World world, BlockPos pos, boolean rule) {
        boolean isPowered = world.isReceivingRedstonePower(pos);
        return isPowered || (QuasiConnectivity.hasQuasiSignal(world, pos) && rule && CarpetSettings.quasiConnectivity != 0);
    }

    /**
     * @return true if the rule flag is 1 or 3.
     * @implNote This is used for disabling certain instances of world.updateNeighbors(), and in principle this returns true for all odd numbers. The only odd numbers present in the flags of the UpdateMode enum entries are BLOCK(1) and BOTH(3).
     */
    public static boolean neighbourUpdateForRule(DoormatSettings.UpdateMode rule) {
        return (rule.getFlags() & 1) == 1;
    }

}
