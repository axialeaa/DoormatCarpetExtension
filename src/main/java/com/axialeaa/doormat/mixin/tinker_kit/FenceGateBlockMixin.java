package com.axialeaa.doormat.mixin.tinker_kit;

import com.axialeaa.doormat.fake.TinkerKitBehaviourSetter;
import com.axialeaa.doormat.mixin.extensibility.AbstractBlockMixin;
import com.axialeaa.doormat.tinker_kit.TinkerKit;
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
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.ModifyArg;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(FenceGateBlock.class)
public class FenceGateBlockMixin extends AbstractBlockMixin implements TinkerKitBehaviourSetter {

    @Shadow @Final public static BooleanProperty POWERED;
    @Shadow @Final public static BooleanProperty OPEN;
    @Shadow @Final private WoodType type;

    @Unique private boolean isPowered = false;

    @Inject(method = "neighborUpdate", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/World;isReceivingRedstonePower(Lnet/minecraft/util/math/BlockPos;)Z", shift = At.Shift.BEFORE), cancellable = true)
    private void defineTinkerKitBehaviour(BlockState state, World world, BlockPos pos, Block sourceBlock, BlockPos sourcePos, boolean notify, CallbackInfo ci) {
        int delay = TinkerKit.getDelay(state, 0);
        this.isPowered = TinkerKit.isReceivingRedstonePower(world, pos, state);

        if (delay == 0)
            getBehaviour(world, pos, state);
        else {
            TickPriority tickPriority = TinkerKit.getTickPriority(state, TickPriority.NORMAL);
            world.scheduleBlockTick(pos, state.getBlock(), delay, tickPriority);
        }

        ci.cancel();
    }

    @WrapOperation(method = "getPlacementState", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/World;isReceivingRedstonePower(Lnet/minecraft/util/math/BlockPos;)Z"))
    private boolean fixPlacement(World instance, BlockPos pos, Operation<Boolean> original) {
        this.isPowered = TinkerKit.isReceivingRedstonePower(instance, pos, instance.getBlockState(pos));
        return isPowered;
    }

    @ModifyArg(method = "onUse", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/World;setBlockState(Lnet/minecraft/util/math/BlockPos;Lnet/minecraft/block/BlockState;I)Z"))
    private int changeUpdateType(int original, @Local(argsOnly = true) BlockState state) {
        return TinkerKit.getFlags(state, original);
    }

    @Override
    public void scheduledTickImpl(BlockState state, ServerWorld world, BlockPos pos, Random random, CallbackInfo ci) {
        getBehaviour(world, pos, state);
    }

    @Override
    public void getBehaviour(World world, BlockPos pos, BlockState state) {
        if (isPowered != state.get(POWERED)) {
            int flags = TinkerKit.getFlags(state, Block.NOTIFY_LISTENERS);
            world.setBlockState(pos, state.with(POWERED, isPowered).with(OPEN, isPowered), flags);

            if (isPowered != state.get(OPEN)) {
                world.playSound(null, pos, isPowered ? this.type.fenceGateOpen() : this.type.fenceGateClose(), SoundCategory.BLOCKS, 1.0F, world.getRandom().nextFloat() * 0.1F + 0.9F);
                world.emitGameEvent(null, isPowered ? GameEvent.BLOCK_OPEN : GameEvent.BLOCK_CLOSE, pos);
            }
        }
    }

}
