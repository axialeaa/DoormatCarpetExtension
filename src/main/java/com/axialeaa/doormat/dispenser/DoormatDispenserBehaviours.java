package com.axialeaa.doormat.dispenser;

import com.axialeaa.doormat.dispenser.behaviour.BuryItemDispenserBehaviour;
import com.axialeaa.doormat.setting.DoormatSettings;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.DispenserBlock;
import net.minecraft.block.dispenser.DispenserBehavior;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.math.BlockPointer;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;

public class DoormatDispenserBehaviours {

    public static DispenserBehavior getCustom(ServerWorld world, BlockPos pos, BlockPointer pointer) {
        Direction facingDir = pointer.state().get(DispenserBlock.FACING);
        BlockPos blockPos = pos.offset(facingDir);

        BlockState blockState = world.getBlockState(blockPos);
        Block block = blockState.getBlock();

        if (DoormatSettings.dispensersBuryItems && BuryItemDispenserBehaviour.MAP.containsKey(block))
            return new BuryItemDispenserBehaviour(block);

        return null;
    }

}
