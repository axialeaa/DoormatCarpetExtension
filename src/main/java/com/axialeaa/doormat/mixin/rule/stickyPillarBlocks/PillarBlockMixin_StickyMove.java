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
public class PillarBlockMixin_StickyMove implements BlockPistonBehaviourInterface {

    @Override
    public boolean isSticky(BlockState state) {
        return DoormatSettings.stickyPillarBlocks.isEnabled();
    }

    @Override
    public boolean isStickyToNeighbor(World level, BlockPos pos, BlockState state, BlockPos neighborPos, BlockState neighborState, Direction dir, Direction moveDir) {
        if (DoormatSettings.stickyPillarBlocks == DoormatSettings.StickyBlockMode.TRUE)
            return neighborState.getBlock() instanceof PillarBlock && dir.getAxis() == neighborState.get(PillarBlock.AXIS);
        else if (DoormatSettings.stickyPillarBlocks == DoormatSettings.StickyBlockMode.STICK_TO_ALL)
            return neighborState.getBlock() instanceof PillarBlock;
        return false;
    }

}
