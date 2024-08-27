package com.axialeaa.doormat.helper;

import net.minecraft.block.BlockState;
import net.minecraft.block.DoorBlock;
import net.minecraft.block.enums.DoorHinge;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.world.World;

public class DoubleDoorHelper {

    public static Direction getConnectedDoorDirection(BlockState state) {
        DoorHinge hinge = state.get(DoorBlock.HINGE);
        Direction facing = state.get(DoorBlock.FACING);

        return hinge == DoorHinge.LEFT ? facing.rotateYClockwise() : facing.rotateYCounterclockwise();
    }

    public static BlockState getConnectedDoorState(World world, BlockPos pos, BlockState state) {
        return world.getBlockState(pos.offset(getConnectedDoorDirection(state)));
    }

}
