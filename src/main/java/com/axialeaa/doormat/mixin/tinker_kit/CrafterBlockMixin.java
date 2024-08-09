package com.axialeaa.doormat.mixin.tinker_kit;

import com.axialeaa.doormat.tinker_kit.TinkerKit;
import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import com.llamalad7.mixinextras.sugar.Local;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.CrafterBlock;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.random.Random;
import net.minecraft.world.World;
import net.minecraft.world.tick.TickPriority;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyArg;

@Mixin(CrafterBlock.class)
public abstract class CrafterBlockMixin extends Block {

    @Shadow protected abstract void scheduledTick(BlockState state, ServerWorld world, BlockPos pos, Random random);

    public CrafterBlockMixin(Settings settings) {
        super(settings);
    }

    @WrapOperation(method = { "neighborUpdate", "onPlaced" }, at = @At(value = "INVOKE", target = "Lnet/minecraft/world/World;scheduleBlockTick(Lnet/minecraft/util/math/BlockPos;Lnet/minecraft/block/Block;I)V"))
    private void changeDelayAndTickPriority(World instance, BlockPos pos, Block block, int i, Operation<Void> original, @Local(argsOnly = true) BlockState state) {
        int delay = TinkerKit.getDelay(state, i);

        if (delay == 0)
            if (instance instanceof ServerWorld serverWorld)
                this.scheduledTick(state, serverWorld, pos, serverWorld.getRandom());
        else {
            TickPriority tickPriority = TinkerKit.getTickPriority(state, TickPriority.NORMAL);
            instance.scheduleBlockTick(pos, block, delay, tickPriority);
        }
    }

    @WrapOperation(method = "neighborUpdate", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/World;isReceivingRedstonePower(Lnet/minecraft/util/math/BlockPos;)Z"))
    private boolean allowQuasiConnectivityOnNeighborUpdate(World instance, BlockPos pos, Operation<Boolean> original, @Local(argsOnly = true) BlockState state) {
        return TinkerKit.isReceivingRedstonePower(instance, pos, state);
    }

    @WrapOperation(method = "getPlacementState", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/World;isReceivingRedstonePower(Lnet/minecraft/util/math/BlockPos;)Z"))
    private boolean allowQuasiConnectivityOnPlacement(World instance, BlockPos pos, Operation<Boolean> original) {
        return TinkerKit.isReceivingRedstonePower(instance, pos, this.getDefaultState());
    }

    @ModifyArg(method = "neighborUpdate", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/World;setBlockState(Lnet/minecraft/util/math/BlockPos;Lnet/minecraft/block/BlockState;I)Z"))
    private int changeUpdateTypeOnNeighborUpdate(int flags, @Local(argsOnly = true) BlockState state) {
        return TinkerKit.getFlags(state, flags);
    }

    @ModifyArg(method = "craft", at = @At(value = "INVOKE", target = "Lnet/minecraft/server/world/ServerWorld;setBlockState(Lnet/minecraft/util/math/BlockPos;Lnet/minecraft/block/BlockState;I)Z"))
    private int changeUpdateTypeOnCraft(int flags, @Local(argsOnly = true) BlockState state) {
        return TinkerKit.getFlags(state, flags);
    }

}
