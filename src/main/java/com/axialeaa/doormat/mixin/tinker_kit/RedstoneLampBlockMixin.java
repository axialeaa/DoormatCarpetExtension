package com.axialeaa.doormat.mixin.tinker_kit;

import com.axialeaa.doormat.tinker_kit.TinkerKit;
import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import com.llamalad7.mixinextras.sugar.Local;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.RedstoneLampBlock;
import net.minecraft.item.ItemPlacementContext;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.random.Random;
import net.minecraft.world.World;
import net.minecraft.world.tick.TickPriority;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyArg;

@Mixin(RedstoneLampBlock.class)
public abstract class RedstoneLampBlockMixin extends Block {

    @Shadow protected abstract void scheduledTick(BlockState state, ServerWorld world, BlockPos pos, Random random);

    public RedstoneLampBlockMixin(Settings settings) {
        super(settings);
    }

    @WrapOperation(method = "getPlacementState", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/World;isReceivingRedstonePower(Lnet/minecraft/util/math/BlockPos;)Z"))
    private boolean allowQuasiConnectivityOnPlacement(World instance, BlockPos pos, Operation<Boolean> original, @Local(argsOnly = true) ItemPlacementContext ctx) {
        return TinkerKit.isReceivingRedstonePower(instance, pos, this.getDefaultState());
    }

    @WrapOperation(method = "neighborUpdate", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/World;isReceivingRedstonePower(Lnet/minecraft/util/math/BlockPos;)Z"))
    private boolean allowQuasiConnectivityOnNeighborUpdate(World instance, BlockPos pos, Operation<Boolean> original, @Local(argsOnly = true) BlockState state) {
        return TinkerKit.isReceivingRedstonePower(instance, pos, state);
    }

    @WrapOperation(method = "scheduledTick", at = @At(value = "INVOKE", target = "Lnet/minecraft/server/world/ServerWorld;isReceivingRedstonePower(Lnet/minecraft/util/math/BlockPos;)Z"))
    private boolean allowQuasiConnectivityOnScheduledTick(ServerWorld instance, BlockPos pos, Operation<Boolean> original, @Local(argsOnly = true) BlockState state) {
        return TinkerKit.isReceivingRedstonePower(instance, pos, state);
    }

    @ModifyArg(method = "neighborUpdate", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/World;setBlockState(Lnet/minecraft/util/math/BlockPos;Lnet/minecraft/block/BlockState;I)Z"))
    private int changeUpdateTypeOnNeighborUpdate(int original, @Local(argsOnly = true) BlockState state) {
        return TinkerKit.getFlags(state, original);
    }

    @ModifyArg(method = "scheduledTick", at = @At(value = "INVOKE", target = "Lnet/minecraft/server/world/ServerWorld;setBlockState(Lnet/minecraft/util/math/BlockPos;Lnet/minecraft/block/BlockState;I)Z"))
    private int changeUpdateTypeOnScheduledTick(int original, @Local(argsOnly = true) BlockState state) {
        return TinkerKit.getFlags(state, original);
    }

    @WrapOperation(method = "neighborUpdate", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/World;scheduleBlockTick(Lnet/minecraft/util/math/BlockPos;Lnet/minecraft/block/Block;I)V"))
    private void changeDelayAndTickPriority(World instance, BlockPos pos, Block block, int i, Operation<Void> original, @Local(argsOnly = true) BlockState state) {
        int delay = TinkerKit.getDelay(state, i);

        if (delay == 0) {
            if (instance instanceof ServerWorld serverWorld)
                this.scheduledTick(state, serverWorld, pos, instance.getRandom());
        }
        else instance.scheduleBlockTick(pos, block, delay, TinkerKit.getTickPriority(state, TickPriority.NORMAL));
    }

}
