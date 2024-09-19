package com.axialeaa.doormat.mixin.tinker_kit;

import com.axialeaa.doormat.mixin.impl.AbstractBlockImplMixin;
import com.axialeaa.doormat.registry.DoormatTinkerTypes;
import com.axialeaa.doormat.tinker_kit.TinkerKitUtils;
import com.llamalad7.mixinextras.injector.wrapmethod.WrapMethod;
import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import net.minecraft.block.BellBlock;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.state.property.BooleanProperty;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.util.math.random.Random;
import net.minecraft.world.World;
import net.minecraft.world.tick.TickPriority;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;

@Mixin(value = BellBlock.class, priority = 1500)
public abstract class BellBlockMixin extends AbstractBlockImplMixin {

    @Shadow @Final public static BooleanProperty POWERED;
    @Shadow public abstract boolean ring(World world, BlockPos pos, @Nullable Direction direction);

    @Unique private boolean isPowered;

    @WrapMethod(method = "neighborUpdate")
    protected void neighborUpdate(BlockState state, World world, BlockPos pos, Block sourceBlock, BlockPos sourcePos, boolean notify, Operation<Void> original) {
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
        else getBehaviour(world, pos, state);
    }

    @Override
    public void scheduledTickImpl(BlockState state, ServerWorld world, BlockPos pos, Random random, Operation<Void> original) {
        this.getBehaviour(world, pos, state);
    }

    @Unique
    private void getBehaviour(World world, BlockPos pos, BlockState state) {
        if (this.isPowered == state.get(POWERED))
            return;

        if (this.isPowered)
            this.ring(world, pos, null);

        Block block = state.getBlock();
        int flags = TinkerKitUtils.getFlags(block, Block.NOTIFY_ALL);

        world.setBlockState(pos, state.with(POWERED, this.isPowered), flags);
    }

}
