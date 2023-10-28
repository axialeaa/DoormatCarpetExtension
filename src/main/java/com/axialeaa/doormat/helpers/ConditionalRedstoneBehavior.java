package com.axialeaa.doormat.helpers;

import carpet.CarpetSettings;
import carpet.helpers.QuasiConnectivity;
import com.axialeaa.doormat.DoormatSettings;
import net.minecraft.block.Block;
import net.minecraft.block.ChainBlock;
import net.minecraft.block.DirtPathBlock;
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
        if (DoormatSettings.parityComparatorsReadThroughChains && block instanceof ChainBlock)
            bl = true;
        if (DoormatSettings.parityComparatorsReadThroughPaths && block instanceof DirtPathBlock)
            bl = true;
        return bl;
    }

    static boolean neighborUpdateOn(DoormatSettings.NeighbourUpdateMode rule) {
        return rule.getFlags() == 1 || rule.getFlags() == 3;
    }

}
