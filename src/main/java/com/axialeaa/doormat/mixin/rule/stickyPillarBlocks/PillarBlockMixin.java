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
        return DoormatSettings.stickyPillarBlocks.isEnabled();
    }

    @Override
    public boolean isStickyToNeighbor(World level, BlockPos pos, BlockState state, BlockPos neighborPos, BlockState neighborState, Direction dir, Direction moveDir) {
        return switch (DoormatSettings.stickyPillarBlocks) {
            case TRUE -> neighborState == state && dir.getAxis() == neighborState.get(PillarBlock.AXIS);
            case STICK_TO_ALL -> neighborState == state;
            default -> false;
        };
    }

}
