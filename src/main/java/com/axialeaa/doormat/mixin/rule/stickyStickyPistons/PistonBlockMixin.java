package com.axialeaa.doormat.mixin.rule.stickyStickyPistons;

import carpet.fakes.BlockPistonBehaviourInterface;
import com.axialeaa.doormat.setting.DoormatSettings;
import net.minecraft.block.BlockState;
import net.minecraft.block.FacingBlock;
import net.minecraft.block.PistonBlock;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;

@Mixin(PistonBlock.class)
public abstract class PistonBlockMixin extends FacingBlock implements BlockPistonBehaviourInterface {

    @Shadow @Final private boolean sticky;

    protected PistonBlockMixin(Settings settings) {
        super(settings);
    }

    @Override
    public boolean isSticky(BlockState state) {
        return DoormatSettings.stickyStickyPistons.enabled() && this.sticky;
    }

    @Override
    public boolean isStickyToNeighbor(World level, BlockPos pos, BlockState state, BlockPos neighborPos, BlockState neighborState, Direction dir, Direction moveDir) {
        return switch (DoormatSettings.stickyStickyPistons) {
            case FALSE -> false;
            case STICK_TO_ALL -> this.sticky;
            case TRUE -> this.sticky && dir == state.get(PistonBlock.FACING);
        };
    }

}
