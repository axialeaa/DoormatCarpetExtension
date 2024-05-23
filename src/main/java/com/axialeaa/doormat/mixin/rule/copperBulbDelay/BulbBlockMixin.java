package com.axialeaa.doormat.mixin.rule.copperBulbDelay;

import com.axialeaa.doormat.DoormatSettings;
import com.axialeaa.doormat.mixin.extensibility.AbstractBlockMixin;
import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import net.minecraft.block.BlockState;
import net.minecraft.block.BulbBlock;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.random.Random;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(BulbBlock.class)
public abstract class BulbBlockMixin extends AbstractBlockMixin {

    @Shadow public abstract void update(BlockState state, ServerWorld world, BlockPos pos);

    /**
     * Schedules a tile tick which later calls the update method, with a delay that matches the rule specification if above 0. If zero, the method will be called directly. This is because a delay of "0" in the scheduleBlockTick() method is the same as 1.
     */
    @WrapOperation(method = { "neighborUpdate", "onBlockAdded" }, at = @At(value = "INVOKE", target = "Lnet/minecraft/block/BulbBlock;update(Lnet/minecraft/block/BlockState;Lnet/minecraft/server/world/ServerWorld;Lnet/minecraft/util/math/BlockPos;)V"))
    private void scheduleTickOn(BulbBlock instance, BlockState state, ServerWorld world, BlockPos pos, Operation<Void> original) {
        if (DoormatSettings.copperBulbDelay > 0)
            world.scheduleBlockTick(pos, instance, DoormatSettings.copperBulbDelay);
        else original.call(instance, state, world, pos);
    }

    @Override
    public void scheduledTickImpl(BlockState state, ServerWorld world, BlockPos pos, Random random, CallbackInfo ci) {
        if (DoormatSettings.copperBulbDelay > 0)
            update(state, world, pos);
    }

}
