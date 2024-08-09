package com.axialeaa.doormat.mixin.tinker_kit;

import com.axialeaa.doormat.tinker_kit.TinkerKit;
import com.llamalad7.mixinextras.injector.ModifyReturnValue;
import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import com.llamalad7.mixinextras.sugar.Local;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.ComparatorBlock;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.random.Random;
import net.minecraft.world.World;
import net.minecraft.world.tick.TickPriority;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;

@Mixin(ComparatorBlock.class)
public abstract class ComparatorBlockMixin {

    @Shadow protected abstract int getUpdateDelayInternal(BlockState state);
    @Shadow protected abstract void scheduledTick(BlockState state, ServerWorld world, BlockPos pos, Random random);

    @WrapOperation(method = "updatePowered", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/World;scheduleBlockTick(Lnet/minecraft/util/math/BlockPos;Lnet/minecraft/block/Block;ILnet/minecraft/world/tick/TickPriority;)V"))
    private void changeDelayAndTickPriority(World instance, BlockPos pos, Block block, int i, TickPriority tickPriority, Operation<Void> original, @Local(argsOnly = true) BlockState state) {
        if (this.getUpdateDelayInternal(state) == 0) {
            if (instance instanceof ServerWorld serverWorld)
                this.scheduledTick(state, serverWorld, pos, instance.getRandom());
        }
        else instance.scheduleBlockTick(pos, block, TinkerKit.getDelay(state, i), tickPriority);
    }

    @ModifyReturnValue(method = "getUpdateDelayInternal", at = @At("RETURN"))
    private int changeDelay(int original, @Local(argsOnly = true) BlockState state) {
        return TinkerKit.getDelay(state, original);
    }

}
