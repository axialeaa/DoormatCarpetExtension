package com.axialeaa.doormat.fake;

import net.minecraft.block.BlockState;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.random.Random;
import net.minecraft.world.World;

/**
 * Adds methods for the purpose of containing redstone component behaviour in a way that can be easily called from {@link net.minecraft.block.Block#scheduledTick(BlockState, ServerWorld, BlockPos, Random)}.
 */
@FunctionalInterface
public interface TinkerKitBehaviourSetter {

    void getBehaviour(World world, BlockPos pos, BlockState state);

}
