package com.axialeaa.doormat.mixin.tinker_kit;

import com.axialeaa.doormat.mixin.impl.AbstractBlockImplMixin;
import com.axialeaa.doormat.registry.DoormatTinkerTypes;
import com.axialeaa.doormat.tinker_kit.TinkerKitUtils;
import com.llamalad7.mixinextras.injector.wrapmethod.WrapMethod;
import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import com.llamalad7.mixinextras.sugar.Local;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.FenceGateBlock;
import net.minecraft.block.WoodType;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.sound.SoundCategory;
import net.minecraft.state.property.BooleanProperty;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.random.Random;
import net.minecraft.world.World;
import net.minecraft.world.event.GameEvent;
import net.minecraft.world.tick.TickPriority;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyArg;

@Mixin(value = FenceGateBlock.class, priority = 1500)
public class FenceGateBlockMixin extends AbstractBlockImplMixin {

    @Shadow @Final public static BooleanProperty POWERED;
    @Shadow @Final public static BooleanProperty OPEN;
    @Shadow @Final private WoodType type;

    @Unique private boolean isPowered = false;

    @WrapOperation(method = "getPlacementState", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/World;isReceivingRedstonePower(Lnet/minecraft/util/math/BlockPos;)Z"))
    private boolean fixPlacement(World instance, BlockPos pos, Operation<Boolean> original) {
        BlockState blockState = instance.getBlockState(pos);
        Block block = blockState.getBlock();

        this.isPowered = TinkerKitUtils.isReceivingRedstonePower(instance, pos, block);

        return isPowered;
    }

    @ModifyArg(method = "onUse", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/World;setBlockState(Lnet/minecraft/util/math/BlockPos;Lnet/minecraft/block/BlockState;I)Z"))
    private int modifyUpdateType(int original, @Local(argsOnly = true) BlockState state) {
        Block block = state.getBlock();
        return TinkerKitUtils.getFlags(block, original);
    }

    @WrapMethod(method = "neighborUpdate")
    protected void scheduleOrCall(BlockState state, World world, BlockPos pos, Block sourceBlock, BlockPos sourcePos, boolean notify, Operation<Void> original) {
        if (world.isClient())
            return;

        Block block = state.getBlock();

        if (!DoormatTinkerTypes.QC.canModify(block)) {
            original.call(state, world, pos, sourceBlock, sourcePos, notify);
            return;
        }

        int delay = TinkerKitUtils.getDelay(block, 0);
        this.isPowered = TinkerKitUtils.isReceivingRedstonePower(world, pos, block);

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
        if (isPowered == state.get(POWERED))
            return;

        Block block = state.getBlock();
        int flags = TinkerKitUtils.getFlags(block, Block.NOTIFY_LISTENERS);

        world.setBlockState(pos, state.with(POWERED, isPowered).with(OPEN, isPowered), flags);

        if (isPowered != state.get(OPEN)) {
            world.playSound(null, pos, isPowered ? this.type.fenceGateOpen() : this.type.fenceGateClose(), SoundCategory.BLOCKS, 1.0F, world.getRandom().nextFloat() * 0.1F + 0.9F);
            world.emitGameEvent(null, isPowered ? GameEvent.BLOCK_OPEN : GameEvent.BLOCK_CLOSE, pos);
        }
    }

}
