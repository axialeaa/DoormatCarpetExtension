package com.axialeaa.doormat.mixin.tinker_kit;

import com.axialeaa.doormat.fake.TinkerKitBehaviourSetter;
import com.axialeaa.doormat.mixin.extensibility.AbstractBlockMixin;
import com.axialeaa.doormat.tinker_kit.TinkerKit;
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
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.ModifyArg;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(DoorBlock.class)
public abstract class DoorBlockMixin extends AbstractBlockMixin implements TinkerKitBehaviourSetter {

    @Shadow @Final public static BooleanProperty POWERED;
    @Shadow @Final public static BooleanProperty OPEN;
    @Shadow @Final public static EnumProperty<DoubleBlockHalf> HALF;
    @Shadow protected abstract void playOpenCloseSound(@Nullable Entity entity, World world, BlockPos pos, boolean open);

    @Unique private boolean isPowered;

    @Inject(method = "neighborUpdate", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/World;isReceivingRedstonePower(Lnet/minecraft/util/math/BlockPos;)Z", shift = At.Shift.BEFORE), cancellable = true)
    private void defineTinkerKitBehaviour(BlockState state, World world, BlockPos pos, Block sourceBlock, BlockPos sourcePos, boolean notify, CallbackInfo ci) {
        int delay = TinkerKit.getDelay(state, 0);
        boolean isLowerHalf = state.get(HALF) == DoubleBlockHalf.LOWER;

        this.isPowered = TinkerKit.isReceivingRedstonePower(world, pos, state, isLowerHalf ? 1 : 0) || (!isLowerHalf && world.isReceivingRedstonePower(pos.down()));

        if (delay == 0)
            getBehaviour(world, pos, state);
        else {
            TickPriority tickPriority = TinkerKit.getTickPriority(state, TickPriority.NORMAL);
            world.scheduleBlockTick(pos, state.getBlock(), delay, tickPriority);
        }

        ci.cancel();
    }

    @WrapOperation(method = "getPlacementState", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/World;isReceivingRedstonePower(Lnet/minecraft/util/math/BlockPos;)Z", ordinal = 0))
    private boolean allowQuasiConnectivity(World instance, BlockPos pos, Operation<Boolean> original) {
        BlockState blockState = instance.getBlockState(pos);
        this.isPowered = TinkerKit.isReceivingRedstonePower(instance, pos, blockState, 1);

        return isPowered;
    }

    @ModifyArg(method = { "onUse", "setOpen" }, at = @At(value = "INVOKE", target = "Lnet/minecraft/world/World;setBlockState(Lnet/minecraft/util/math/BlockPos;Lnet/minecraft/block/BlockState;I)Z"))
    private int changeUpdateType(int original, @Local(argsOnly = true) BlockState state) {
        return TinkerKit.getFlags(state, original) | Block.REDRAW_ON_MAIN_THREAD;
    }

    @Override
    public void scheduledTickImpl(BlockState state, ServerWorld world, BlockPos pos, Random random, CallbackInfo ci) {
        getBehaviour(world, pos, state);
    }

    @Override
    public void getBehaviour(World world, BlockPos pos, BlockState state) {
        if (isPowered != state.get(POWERED)) {
            BlockState blockState = state;

            if (state.get(OPEN) != isPowered) {
                blockState = state.with(OPEN, isPowered);

                this.playOpenCloseSound(null, world, pos, isPowered);
                world.emitGameEvent(null, isPowered ? GameEvent.BLOCK_OPEN : GameEvent.BLOCK_CLOSE, pos);
            }

            int flags = TinkerKit.getFlags(state, Block.NOTIFY_LISTENERS);
            world.setBlockState(pos, blockState.with(POWERED, isPowered), flags);
        }
    }

}
