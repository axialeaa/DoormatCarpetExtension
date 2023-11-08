package com.axialeaa.doormat.interfaces;

import com.axialeaa.doormat.mixin.integrators.RedstoneWireBlockMixin;
import net.minecraft.block.BlockState;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.world.World;

/**
 * Opt-in interface for easily configurable redstone dust behaviour via {@link RedstoneWireBlockMixin}.
 */
public interface BlockDustBehaviourInterface {

    /**
     * @return whether dust power can travel down this block.
     */
    boolean dustCanDescend(World world, BlockPos pos, BlockState state, Direction direction);

}
