package com.axialeaa.doormat.helpers;

import carpet.CarpetSettings;
import carpet.helpers.QuasiConnectivity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public interface ConditionalRedstoneBehavior {

    static boolean quasiConnectOn(boolean condition, World world, BlockPos pos) {
        boolean isPowered = world.isReceivingRedstonePower(pos);
        return condition && CarpetSettings.quasiConnectivity != 0 ?
            QuasiConnectivity.hasQuasiSignal(world, pos) || isPowered :
            isPowered;
    }

}
