package com.axialeaa.doormat.mixin.tinker_kit;

import com.axialeaa.doormat.mixin.extensibility.AbstractBlockMixin;
import com.axialeaa.doormat.tinker_kit.TinkerKit;
import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import com.llamalad7.mixinextras.sugar.Local;
import net.minecraft.block.BlockState;
import net.minecraft.block.BulbBlock;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.random.Random;
import net.minecraft.world.tick.TickPriority;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyArg;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(BulbBlock.class)
public abstract class BulbBlockMixin extends AbstractBlockMixin {

    @Shadow public abstract void update(BlockState state, ServerWorld world, BlockPos pos);

    @WrapOperation(method = { "neighborUpdate", "onBlockAdded" }, at = @At(value = "INVOKE", target = "Lnet/minecraft/block/BulbBlock;update(Lnet/minecraft/block/BlockState;Lnet/minecraft/server/world/ServerWorld;Lnet/minecraft/util/math/BlockPos;)V"))
    private void scheduleTickOn(BulbBlock instance, BlockState state, ServerWorld world, BlockPos pos, Operation<Void> original) {
        int delay = TinkerKit.getDelay(state, 0);

        if (delay == 0)
            original.call(instance, state, world, pos);
        else {
            TickPriority tickPriority = TinkerKit.getTickPriority(state, TickPriority.NORMAL);
            world.scheduleBlockTick(pos, instance, delay, tickPriority);
        }
    }

    @ModifyArg(method = "update", at = @At(value = "INVOKE", target = "Lnet/minecraft/server/world/ServerWorld;setBlockState(Lnet/minecraft/util/math/BlockPos;Lnet/minecraft/block/BlockState;I)Z"))
    private int changeUpdateType(int flags, @Local(argsOnly = true) BlockState state) {
        return TinkerKit.getFlags(state, flags);
    }

    @WrapOperation(method = "update", at = @At(value = "INVOKE", target = "Lnet/minecraft/server/world/ServerWorld;isReceivingRedstonePower(Lnet/minecraft/util/math/BlockPos;)Z"))
    private boolean allowQuasiConnectivity(ServerWorld instance, BlockPos pos, Operation<Boolean> original, @Local(argsOnly = true) BlockState state) {
        return TinkerKit.isReceivingRedstonePower(instance, pos, state);
    }

    @Override
    public void scheduledTickImpl(BlockState state, ServerWorld world, BlockPos pos, Random random, CallbackInfo ci) {
        this.update(state, world, pos);
    }

}
