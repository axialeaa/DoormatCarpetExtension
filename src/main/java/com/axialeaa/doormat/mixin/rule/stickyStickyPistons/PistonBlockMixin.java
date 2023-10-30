package com.axialeaa.doormat.mixin.rule.stickyStickyPistons;

import carpet.fakes.BlockPistonBehaviourInterface;
import com.axialeaa.doormat.DoormatSettings;
import net.minecraft.block.BlockState;
import net.minecraft.block.PistonBlock;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;

@Mixin(PistonBlock.class)
public class PistonBlockMixin implements BlockPistonBehaviourInterface {

    @Shadow @Final private boolean sticky;

    @Override
    public boolean isSticky(BlockState state) {
        return DoormatSettings.stickyStickyPistons.enabled() && this.sticky;
    }

    @Override
    public boolean isStickyToNeighbor(World level, BlockPos pos, BlockState state, BlockPos neighborPos, BlockState neighborState, Direction dir, Direction moveDir) {
        return switch (DoormatSettings.stickyStickyPistons) {
            case TRUE -> dir == state.get(PistonBlock.FACING);
            // if the rule is true, stick to the neighbour if the checked neighbour direction is the same as the piston facing direction
            case STICK_TO_ALL -> true;
            // if the rule is stick_to_all, stick to the neighbour always
            default -> false;
            // otherwise don't stick to the neighbour
        };
    }

}
