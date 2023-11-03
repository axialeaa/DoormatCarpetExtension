package com.axialeaa.doormat.helpers;

import carpet.CarpetSettings;
import carpet.helpers.QuasiConnectivity;
import com.axialeaa.doormat.DoormatSettings;
import net.minecraft.block.*;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.world.World;

public interface ConditionalRedstoneBehavior {

    boolean canConnectToWire(World world, BlockPos pos, BlockState state, Direction direction);

    boolean canReadThroughBlock(Block block);

    static boolean quasiConnectOn(boolean rule, World world, BlockPos pos) {
        boolean isPowered = world.isReceivingRedstonePower(pos);
        return rule && CarpetSettings.quasiConnectivity != 0 ?
            QuasiConnectivity.hasQuasiSignal(world, pos) || isPowered :
            isPowered;
    }

    static boolean neighborUpdateOn(DoormatSettings.NeighbourUpdateMode rule) {
        return rule.getFlags() == 1 || rule.getFlags() == 3;
    }

}
