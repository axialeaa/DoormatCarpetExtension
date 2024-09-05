package com.axialeaa.doormat.mixin.rule.stickyPillarBlocks;

import carpet.fakes.BlockPistonBehaviourInterface;
import com.axialeaa.doormat.setting.DoormatSettings;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.PillarBlock;
import net.minecraft.state.property.EnumProperty;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;

@Mixin(PillarBlock.class)
public class PillarBlockMixin implements BlockPistonBehaviourInterface {

    @Shadow @Final public static EnumProperty<Direction.Axis> AXIS;

    @Override
    public boolean isSticky(BlockState state) {
        return DoormatSettings.stickyPillarBlocks.enabled();
    }

    @Override
    public boolean isStickyToNeighbor(World level, BlockPos pos, BlockState state, BlockPos neighborPos, BlockState neighborState, Direction dir, Direction moveDir) {
        Block block = state.getBlock();
        boolean isBlock = neighborState.isOf(block);

        return switch (DoormatSettings.stickyPillarBlocks) {
            case FALSE -> false;
            case STICK_TO_ALL -> isBlock;
            case TRUE -> isBlock && dir.getAxis() == neighborState.get(AXIS);
        };
    }

}
