package com.axialeaa.doormat.mixin.tinker_kit;

import com.axialeaa.doormat.mixin.extensibility.AbstractBlockMixin;
import com.axialeaa.doormat.tinker_kit.TinkerKit;
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
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(HopperBlock.class)
public class HopperBlockMixin extends AbstractBlockMixin {

    @Shadow @Final public static BooleanProperty ENABLED;

    @Unique private boolean isPowered;

    @WrapOperation(method = "onBlockAdded", at = @At(value = "INVOKE", target = "Lnet/minecraft/block/HopperBlock;updateEnabled(Lnet/minecraft/world/World;Lnet/minecraft/util/math/BlockPos;Lnet/minecraft/block/BlockState;)V"))
    private void fixPlacement(HopperBlock instance, World world, BlockPos pos, BlockState state, Operation<Void> original) {
        this.isPowered = TinkerKit.isReceivingRedstonePower(world, pos, instance);
        getBehaviour(world, pos, state);
    }

    @Inject(method = "neighborUpdate", at = @At("HEAD"), cancellable = true)
    private void scheduleOrCall(BlockState state, World world, BlockPos pos, Block sourceBlock, BlockPos sourcePos, boolean notify, CallbackInfo ci) {
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
        getBehaviour(world, pos, state);
    }

    @Unique
    public void getBehaviour(World world, BlockPos pos, BlockState state) {
        if (isPowered != state.get(ENABLED))
            return;

        Block block = state.getBlock();
        int flags = TinkerKit.getFlags(block, Block.NOTIFY_LISTENERS);

        world.setBlockState(pos, state.with(ENABLED, !isPowered), flags);
    }

}
