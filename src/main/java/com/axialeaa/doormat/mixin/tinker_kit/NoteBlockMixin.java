package com.axialeaa.doormat.mixin.tinker_kit;

import com.axialeaa.doormat.fake.TinkerKitBehaviourSetter;
import com.axialeaa.doormat.mixin.extensibility.AbstractBlockMixin;
import com.axialeaa.doormat.tinker_kit.TinkerKit;
import com.llamalad7.mixinextras.sugar.Local;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.NoteBlock;
import net.minecraft.entity.Entity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.state.property.BooleanProperty;
import net.minecraft.util.math.BlockPos;
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
import org.spongepowered.asm.mixin.injection.ModifyArg;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(NoteBlock.class)
public abstract class NoteBlockMixin extends AbstractBlockMixin implements TinkerKitBehaviourSetter {

    @Shadow @Final public static BooleanProperty POWERED;
    @Shadow protected abstract void playNote(@Nullable Entity entity, BlockState state, World world, BlockPos pos);

    @Unique private boolean isPowered = false;

    @Inject(method = "neighborUpdate", at = @At(value = "HEAD"), cancellable = true)
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

    @ModifyArg(method = "onUse", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/World;setBlockState(Lnet/minecraft/util/math/BlockPos;Lnet/minecraft/block/BlockState;I)Z"), index = 2)
    private int changeUpdateType_onUse(int original, @Local(argsOnly = true) BlockState state) {
        return TinkerKit.getFlags(state, original);
    }

    @Override
    public void scheduledTickImpl(BlockState state, ServerWorld world, BlockPos pos, Random random, CallbackInfo ci) {
        getBehaviour(world, pos, state);
    }

    @Override
    public void getBehaviour(World world, BlockPos pos, BlockState state) {
        if (isPowered != state.get(POWERED)) {
            if (isPowered)
                this.playNote(null, state, world, pos);

            int flags = TinkerKit.getFlags(state, Block.NOTIFY_ALL);
            world.setBlockState(pos, state.with(POWERED, isPowered), flags);
        }
    }

}
