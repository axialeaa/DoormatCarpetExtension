package com.axialeaa.doormat.mixin.tinker_kit;

import com.axialeaa.doormat.fake.TinkerKitBehaviourSetter;
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
public class HopperBlockMixin extends AbstractBlockMixin implements TinkerKitBehaviourSetter {

    @Shadow @Final public static BooleanProperty ENABLED;

    @Unique private boolean isPowered = false;

    @WrapOperation(method = "onBlockAdded", at = @At(value = "INVOKE", target = "Lnet/minecraft/block/HopperBlock;updateEnabled(Lnet/minecraft/world/World;Lnet/minecraft/util/math/BlockPos;Lnet/minecraft/block/BlockState;)V"))
    private void setPowered(HopperBlock instance, World world, BlockPos pos, BlockState state, Operation<Void> original) {
        this.isPowered = TinkerKit.isReceivingRedstonePower(world, pos, state);
        getBehaviour(world, pos, state);
    }

    @Inject(method = "neighborUpdate", at = @At("HEAD"), cancellable = true)
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

    @Override
    public void scheduledTickImpl(BlockState state, ServerWorld world, BlockPos pos, Random random, CallbackInfo ci) {
        getBehaviour(world, pos, state);
    }

    @Override
    public void getBehaviour(World world, BlockPos pos, BlockState state) {
        if (isPowered == state.get(ENABLED))
            world.setBlockState(pos, state.with(ENABLED, isPowered), TinkerKit.getFlags(state, Block.NOTIFY_LISTENERS));
    }

}
