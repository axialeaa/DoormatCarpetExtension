package com.axialeaa.doormat.mixin.rule.openDoubleDoors;

import com.axialeaa.doormat.DoormatSettings;
import net.minecraft.block.BlockState;
import net.minecraft.block.DoorBlock;
import net.minecraft.block.enums.DoorHinge;
import net.minecraft.block.enums.DoubleBlockHalf;
import net.minecraft.entity.Entity;
import net.minecraft.state.property.BooleanProperty;
import net.minecraft.state.property.DirectionProperty;
import net.minecraft.state.property.EnumProperty;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.world.World;
import net.minecraft.world.WorldAccess;
import net.minecraft.world.event.GameEvent;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(DoorBlock.class)
public abstract class DoorBlockMixin {

    @Shadow @Final public static EnumProperty<DoorHinge> HINGE;
    @Shadow @Final public static EnumProperty<DoubleBlockHalf> HALF;
    @Shadow @Final public static DirectionProperty FACING;
    @Shadow @Final public static BooleanProperty OPEN;

    @Shadow protected abstract void playOpenCloseSound(@Nullable Entity entity, World world, BlockPos pos, boolean open);

    @Inject(method = "getStateForNeighborUpdate", at = @At(value = "INVOKE", target = "Lnet/minecraft/block/BlockState;get(Lnet/minecraft/state/property/Property;)Ljava/lang/Comparable;", shift = At.Shift.BEFORE), cancellable = true)
    private void openConnectedDoorOnShapeUpdate(BlockState state, Direction direction, BlockState neighborState, WorldAccess world, BlockPos pos, BlockPos neighborPos, CallbackInfoReturnable<BlockState> cir) {
        if (!DoormatSettings.openDoubleDoors || !(neighborState.getBlock() instanceof DoorBlock))
            return;

        DoorHinge hinge = state.get(HINGE);
        DoubleBlockHalf half = state.get(HALF);

        DoorHinge neighborHinge = neighborState.get(HINGE);
        DoubleBlockHalf neighborHalf = neighborState.get(HALF);

        boolean open = state.get(OPEN);
        boolean neighborOpen = neighborState.get(OPEN);

        if (hinge == neighborHinge || half != neighborHalf || open == neighborOpen || direction != getNeighborDoorDirection(state))
            return;

        this.playOpenCloseSound(null, (World) world, neighborPos, neighborOpen);
        world.emitGameEvent(null, neighborOpen ? GameEvent.BLOCK_OPEN : GameEvent.BLOCK_CLOSE, neighborPos);

        cir.setReturnValue(state.with(OPEN, neighborOpen));
    }

    @Unique
    private static Direction getNeighborDoorDirection(BlockState state) {
        DoorHinge hinge = state.get(HINGE);
        Direction facing = state.get(FACING);

        return hinge == DoorHinge.LEFT ? facing.rotateYClockwise() : facing.rotateYCounterclockwise();
    }

}
