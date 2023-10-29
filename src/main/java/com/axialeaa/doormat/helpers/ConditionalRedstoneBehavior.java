package com.axialeaa.doormat.helpers;

import carpet.CarpetSettings;
import carpet.helpers.QuasiConnectivity;
import com.axialeaa.doormat.DoormatSettings;
import net.minecraft.block.Block;
import net.minecraft.block.ChainBlock;
import net.minecraft.block.DirtPathBlock;
import net.minecraft.block.PistonBlock;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public interface ConditionalRedstoneBehavior {

    static boolean quasiConnectOn(boolean rule, World world, BlockPos pos) {
        boolean isPowered = world.isReceivingRedstonePower(pos);
        return rule && CarpetSettings.quasiConnectivity != 0 ?
            QuasiConnectivity.hasQuasiSignal(world, pos) || isPowered :
            isPowered;
    }

    static boolean canReadThroughBlock(Block block) {
        boolean bl = false;
        if (DoormatSettings.comparatorsReadThroughChains && block instanceof ChainBlock)
            bl = true;
        if (DoormatSettings.comparatorsReadThroughPaths && block instanceof DirtPathBlock)
            bl = true;
        if (DoormatSettings.comparatorsReadThroughPistons && block instanceof PistonBlock)
            bl = true;
        return bl;
    }

    static boolean neighborUpdateOn(DoormatSettings.NeighbourUpdateMode rule) {
        return rule.getFlags() == 1 || rule.getFlags() == 3;
    }

}
