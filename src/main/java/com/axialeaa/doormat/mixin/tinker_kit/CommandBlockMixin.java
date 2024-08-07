package com.axialeaa.doormat.mixin.tinker_kit;

import com.axialeaa.doormat.fake.CommandBlockWorldTimeChecker;
import com.axialeaa.doormat.tinker_kit.TinkerKit;
import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import com.llamalad7.mixinextras.sugar.Local;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.CommandBlock;
import net.minecraft.block.entity.CommandBlockBlockEntity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.random.Random;
import net.minecraft.world.World;
import net.minecraft.world.tick.TickPriority;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;

@Mixin(CommandBlock.class)
public abstract class CommandBlockMixin {

    @Shadow protected abstract void scheduledTick(BlockState state, ServerWorld world, BlockPos pos, Random random);

    @WrapOperation(method = "neighborUpdate", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/World;scheduleBlockTick(Lnet/minecraft/util/math/BlockPos;Lnet/minecraft/block/Block;I)V"))
    private void changeDelayAndTickPriority(World instance, BlockPos pos, Block block, int i, Operation<Void> original, @Local(argsOnly = true) BlockState state, @Local CommandBlockBlockEntity commandBlockBlockEntity) {
        int delay = TinkerKit.getDelay(state, i);

        if (delay == 0) {
            CommandBlockWorldTimeChecker checker = (CommandBlockWorldTimeChecker) commandBlockBlockEntity.getCommandExecutor();
            checker.setShouldCheckTime(false);

            if (instance instanceof ServerWorld serverWorld)
                this.scheduledTick(state, serverWorld, pos, serverWorld.getRandom());
        }
        else {
            TickPriority tickPriority = TinkerKit.getTickPriority(state, TickPriority.NORMAL);
            instance.scheduleBlockTick(pos, block, delay, tickPriority);
        }
    }

    @WrapOperation(method = { "neighborUpdate", "onPlaced" }, at = @At(value = "INVOKE", target = "Lnet/minecraft/world/World;isReceivingRedstonePower(Lnet/minecraft/util/math/BlockPos;)Z"))
    private boolean allowQuasiConnectivity(World instance, BlockPos pos, Operation<Boolean> original, @Local(argsOnly = true) BlockState state) {
        return TinkerKit.isReceivingRedstonePower(instance, pos, state);
    }

    @WrapOperation(method = "scheduledTick", at = @At(value = "INVOKE", target = "Lnet/minecraft/server/world/ServerWorld;scheduleBlockTick(Lnet/minecraft/util/math/BlockPos;Lnet/minecraft/block/Block;I)V"))
    private void changeDelayAndTickPriority(ServerWorld instance, BlockPos pos, Block block, int i, Operation<Void> original, @Local(argsOnly = true) BlockState state, @Local(argsOnly = true) Random random) {
        int delay = Math.max(TinkerKit.getDelay(state, i), 1);
        TickPriority tickPriority = TinkerKit.getTickPriority(state, TickPriority.NORMAL);

        instance.scheduleBlockTick(pos, block, delay, tickPriority);
    }

}
