package com.axialeaa.doormat.mixin.tinker_kit;

import com.axialeaa.doormat.mixin.impl.AbstractBlockImplMixin;
import com.axialeaa.doormat.registry.DoormatTinkerTypes;
import com.axialeaa.doormat.tinker_kit.TinkerKitUtils;
import com.llamalad7.mixinextras.injector.wrapmethod.WrapMethod;
import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.HopperBlock;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.state.property.BooleanProperty;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.random.Random;
import net.minecraft.world.World;
import net.minecraft.world.tick.TickPriority;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;

@Mixin(value = HopperBlock.class, priority = 1500)
public class HopperBlockMixin extends AbstractBlockImplMixin {

    @Shadow @Final public static BooleanProperty ENABLED;

    @Unique private boolean isPowered;

    @WrapOperation(method = "onBlockAdded", at = @At(value = "INVOKE", target = "Lnet/minecraft/block/HopperBlock;updateEnabled(Lnet/minecraft/world/World;Lnet/minecraft/util/math/BlockPos;Lnet/minecraft/block/BlockState;)V"))
    private void fixPlacement(HopperBlock instance, World world, BlockPos pos, BlockState state, Operation<Void> original) {
        this.isPowered = TinkerKitUtils.isReceivingRedstonePower(world, pos, instance);
        getBehaviour(world, pos, state);
    }

    @WrapMethod(method = "neighborUpdate")
    protected void scheduleOrCall(BlockState state, World world, BlockPos pos, Block sourceBlock, BlockPos sourcePos, boolean notify, Operation<Void> original) {
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
        if (this.isPowered != state.get(ENABLED))
            return;

        Block block = state.getBlock();
        int flags = TinkerKitUtils.getFlags(block, Block.NOTIFY_LISTENERS);

        world.setBlockState(pos, state.with(ENABLED, !this.isPowered), flags);
    }

}
