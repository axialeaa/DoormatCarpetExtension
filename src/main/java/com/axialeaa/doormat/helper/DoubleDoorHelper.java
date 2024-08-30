package com.axialeaa.doormat.helper;

import net.minecraft.block.BlockState;
import net.minecraft.block.DoorBlock;
import net.minecraft.block.enums.DoorHinge;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.world.World;
import org.jetbrains.annotations.Nullable;

public class DoubleDoorHelper {

    public static @Nullable Direction getConnectedDoorDirection(BlockState state) {
        if (!(state.getBlock() instanceof DoorBlock))
            return null;

        DoorHinge hinge = state.get(DoorBlock.HINGE);
        Direction facing = state.get(DoorBlock.FACING);

        return hinge == DoorHinge.LEFT ? facing.rotateYClockwise() : facing.rotateYCounterclockwise();
    }

    public static @Nullable BlockState getConnectedDoorState(World world, BlockPos pos, BlockState state) {
        Direction direction = getConnectedDoorDirection(state);

        if (direction == null)
            return null;

        return world.getBlockState(pos.offset(direction));
    }

}
