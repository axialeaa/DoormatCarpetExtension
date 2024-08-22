package com.axialeaa.doormat.mixin.tinker_kit;

import com.axialeaa.doormat.mixin.extensibility.AbstractBlockMixin;
import com.axialeaa.doormat.tinker_kit.TinkerKit;
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
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(BellBlock.class)
public abstract class BellBlockMixin extends AbstractBlockMixin {

    @Shadow @Final public static BooleanProperty POWERED;
    @Shadow public abstract boolean ring(World world, BlockPos pos, @Nullable Direction direction);

    @Unique private boolean isPowered;

    @Inject(method = "neighborUpdate", at = @At("HEAD"), cancellable = true)
    private void allowQuasiConnectivity(BlockState state, World world, BlockPos pos, Block sourceBlock, BlockPos sourcePos, boolean notify, CallbackInfo ci) {
        Block block = state.getBlock();
        int delay = TinkerKit.getDelay(block, 0);

        this.isPowered = TinkerKit.isReceivingRedstonePower(world, pos, block);

        if (delay > 0) {
            TickPriority tickPriority = TinkerKit.getTickPriority(block);
            world.scheduleBlockTick(pos, block, delay, tickPriority);
        }
        else getBehaviour(world, pos, state);

        ci.cancel();
    }

    @Override
    public void scheduledTickImpl(BlockState state, ServerWorld world, BlockPos pos, Random random, CallbackInfo ci) {
        this.getBehaviour(world, pos, state);
    }

    @Unique
    private void getBehaviour(World world, BlockPos pos, BlockState state) {
        if (isPowered == state.get(POWERED))
            return;

        if (isPowered)
            this.ring(world, pos, null);

        Block block = state.getBlock();
        int flags = TinkerKit.getFlags(block, Block.NOTIFY_ALL);

        world.setBlockState(pos, state.with(POWERED, isPowered), flags);
    }

}
