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

    @Override
    public boolean isStickyToNeighbor(World level, BlockPos pos, BlockState state, BlockPos neighborPos, BlockState neighborState, Direction dir, Direction moveDir) {
        return switch (DoormatSettings.stickyPillarBlocks) {
            case TRUE -> neighborState.isOf(state.getBlock()) && dir.getAxis() == neighborState.get(PillarBlock.AXIS);
            // if the rule is true, stick to the neighbour if the blocks are the same, and the checked neighbour direction is of the same axis as the pillar block
            case STICK_TO_ALL -> neighborState.isOf(state.getBlock());
            // if the rule is stick_to_all, stick to the neighbour if the blocks are the same
            default -> false;
            // otherwise don't stick to the neighbour
        };
    }

}
