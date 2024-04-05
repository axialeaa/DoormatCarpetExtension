package com.axialeaa.doormat.mixin.rule.stickyPillarBlocks;

import carpet.fakes.BlockPistonBehaviourInterface;
import com.axialeaa.doormat.DoormatSettings;
import net.minecraft.block.BlockState;
import net.minecraft.block.PillarBlock;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;

@Mixin(PillarBlock.class)
public class PillarBlockMixin implements BlockPistonBehaviourInterface {

    @Override
    public boolean isSticky(BlockState state) {
        return DoormatSettings.stickyPillarBlocks.enabled();
    }

    /**
     * @param level the world
     * @param pos the position of the block from which the check is performed
     * @param state the state of the block from which the check is performed
     * @param neighborPos the position of the neighbouring block
     * @param neighborState the blockstate of the neighbouring block
     * @param dir the direction of the neighbour stick check
     * @param moveDir the movement direction of the piston
     * @return if set to true, true if the blocks are the same and the checked neighbour direction is of the same axis as the pillar block. If stick_to_all, true if the blocks are the same otherwise false.
     */
    @Override
    public boolean isStickyToNeighbor(World level, BlockPos pos, BlockState state, BlockPos neighborPos, BlockState neighborState, Direction dir, Direction moveDir) {
        return switch (DoormatSettings.stickyPillarBlocks) {
            case TRUE -> neighborState.isOf(state.getBlock()) && dir.getAxis() == neighborState.get(PillarBlock.AXIS);
            case STICK_TO_ALL -> neighborState.isOf(state.getBlock());
            default -> false;
        };
    }

}
