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
        return DoormatSettings.stickyStickyPistons.isEnabled() && this.sticky;
    }

    @Override
    public boolean isStickyToNeighbor(World level, BlockPos pos, BlockState state, BlockPos neighborPos, BlockState neighborState, Direction dir, Direction moveDir) {
        if (DoormatSettings.stickyStickyPistons == DoormatSettings.StickyBlockMode.TRUE)
            return dir == state.get(PistonBlock.FACING);
        else
            return DoormatSettings.stickyStickyPistons == DoormatSettings.StickyBlockMode.STICK_TO_ALL;
    }

}
