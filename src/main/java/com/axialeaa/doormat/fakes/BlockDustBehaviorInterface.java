package com.axialeaa.doormat.fakes;

import net.minecraft.block.BlockState;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.world.World;

public interface BlockDustBehaviorInterface {

    boolean dustCanDescend(World world, BlockPos pos, BlockState state, Direction direction);

}
