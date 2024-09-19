package com.axialeaa.doormat.mixin.tinker_kit;

import com.axialeaa.doormat.helper.DoubleDoorHelper;
import com.axialeaa.doormat.mixin.impl.AbstractBlockImplMixin;
import com.axialeaa.doormat.registry.DoormatTinkerTypes;
import com.axialeaa.doormat.setting.DoormatSettings;
import com.axialeaa.doormat.tinker_kit.TinkerKitUtils;
import com.llamalad7.mixinextras.injector.wrapmethod.WrapMethod;
import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import com.llamalad7.mixinextras.sugar.Local;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.DoorBlock;
import net.minecraft.block.enums.DoubleBlockHalf;
import net.minecraft.entity.Entity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.state.property.BooleanProperty;
import net.minecraft.state.property.EnumProperty;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.util.math.random.Random;
import net.minecraft.world.World;
import net.minecraft.world.event.GameEvent;
import net.minecraft.world.tick.TickPriority;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyArg;

@Mixin(value = DoorBlock.class, priority = 1500)
public abstract class DoorBlockMixin extends AbstractBlockImplMixin {

    @Shadow @Final public static BooleanProperty POWERED;
    @Shadow @Final public static BooleanProperty OPEN;
    @Shadow @Final public static EnumProperty<DoubleBlockHalf> HALF;

    @Shadow protected abstract void playOpenCloseSound(@Nullable Entity entity, World world, BlockPos pos, boolean open);

    @Unique private boolean isPowered;

    @WrapOperation(method = "getPlacementState", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/World;isReceivingRedstonePower(Lnet/minecraft/util/math/BlockPos;)Z", ordinal = 0))
    private boolean allowQuasiConnectivity(World instance, BlockPos pos, Operation<Boolean> original) {
        BlockState blockState = instance.getBlockState(pos);
        this.isPowered = isDoorPowered(instance, pos, blockState);

        return this.isPowered || isConnectedDoorPowered(instance, pos, blockState);
    }

    @ModifyArg(method = { "onUse", "setOpen" }, at = @At(value = "INVOKE", target = "Lnet/minecraft/world/World;setBlockState(Lnet/minecraft/util/math/BlockPos;Lnet/minecraft/block/BlockState;I)Z"))
    private int modifyUpdateType(int original, @Local(argsOnly = true) BlockState state) {
        Block block = state.getBlock();
        return TinkerKitUtils.getFlags(block, original) | Block.REDRAW_ON_MAIN_THREAD;
    }

    @WrapMethod(method = "neighborUpdate")
    protected void scheduleOrCall(BlockState state, World world, BlockPos pos, Block sourceBlock, BlockPos sourcePos, boolean notify, Operation<Void> original) {
        Block block = state.getBlock();

        if (!DoormatTinkerTypes.QC.canModify(block)) {
            original.call(state, world, pos, sourceBlock, sourcePos, notify);
            return;
        }

        int delay = TinkerKitUtils.getDelay(block, 0);
        this.isPowered = isDoorPowered(world, pos, state);

        if (delay > 0) {
            TickPriority tickPriority = TinkerKitUtils.getTickPriority(block);
            world.scheduleBlockTick(pos, block, delay, tickPriority);
        }
        else this.getBehaviour(world, pos, state);
    }

    @Override
    public void scheduledTickImpl(BlockState state, ServerWorld world, BlockPos pos, Random random, Operation<Void> original) {
        this.getBehaviour(world, pos, state);
        original.call(state, world, pos, random);
    }

    @Unique
    public void getBehaviour(World world, BlockPos pos, BlockState state) {
        BlockState blockState = state;

        if (this.isPowered == blockState.get(POWERED))
            return;

        boolean bl = this.isPowered || isConnectedDoorPowered(world, pos, blockState);

        if (bl != state.get(OPEN)) {
            blockState = state.with(OPEN, bl);

            this.playOpenCloseSound(null, world, pos, bl);
            world.emitGameEvent(null, bl ? GameEvent.BLOCK_OPEN : GameEvent.BLOCK_CLOSE, pos);
        }

        Block block = state.getBlock();
        int flags = TinkerKitUtils.getFlags(block, Block.NOTIFY_LISTENERS);

        world.setBlockState(pos, blockState.with(POWERED, this.isPowered), flags);
    }

    @Unique
    public boolean isDoorPowered(World world, BlockPos pos, BlockState state) {
        Block block = state.getBlock();

        if (!(block instanceof DoorBlock))
            return false;

        boolean lowerHalf = state.get(HALF) == DoubleBlockHalf.LOWER;

        return TinkerKitUtils.isReceivingRedstonePower(world, pos, block, lowerHalf ? 1 : 0) || (!lowerHalf && world.isReceivingRedstonePower(pos.down()));
    }

    @Unique
    public boolean isConnectedDoorPowered(World world, BlockPos pos, BlockState state) {
        if (!DoormatSettings.openDoubleDoors || !(state.getBlock() instanceof DoorBlock))
            return false;

        Direction direction = DoubleDoorHelper.getConnectedDoorDirection(state);
        BlockPos offset = pos.offset(direction);

        BlockState blockState = DoubleDoorHelper.getConnectedDoorState(world, pos, state);

        if (blockState == null)
            return false;

        Block block = blockState.getBlock();
        boolean lowerHalf = blockState.get(HALF) == DoubleBlockHalf.LOWER;

        return TinkerKitUtils.isReceivingRedstonePower(world, offset, block, lowerHalf ? 1 : 0) || (!lowerHalf && world.isReceivingRedstonePower(offset.down()));
    }

}
