package com.axialeaa.doormat.mixin.tinker_kit;

import com.axialeaa.doormat.tinker_kit.TinkerKit;
import com.bawnorton.mixinsquared.TargetHandler;
import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import com.llamalad7.mixinextras.sugar.Local;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.DispenserBlock;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.random.Random;
import net.minecraft.world.RedstoneView;
import net.minecraft.world.World;
import net.minecraft.world.tick.TickPriority;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyArg;

@Mixin(value = DispenserBlock.class, priority = 1500)
public abstract class DispenserBlockMixin {

    @Shadow protected abstract void scheduledTick(BlockState state, ServerWorld world, BlockPos pos, Random random);

    @WrapOperation(method = "neighborUpdate", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/World;scheduleBlockTick(Lnet/minecraft/util/math/BlockPos;Lnet/minecraft/block/Block;I)V"))
    private void changeDelayAndTickPriority(World instance, BlockPos pos, Block block, int i, Operation<Void> original, @Local(argsOnly = true) BlockState state) {
        if (TinkerKit.getDelay(state, i) == 0)
            this.scheduledTick(state, (ServerWorld) instance, pos, instance.getRandom());
        else instance.scheduleBlockTick(pos, block, TinkerKit.getDelay(state, i), TinkerKit.getTickPriority(state, TickPriority.NORMAL));
    }

    @ModifyArg(method = "neighborUpdate", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/World;setBlockState(Lnet/minecraft/util/math/BlockPos;Lnet/minecraft/block/BlockState;I)Z"))
    private int changeUpdateType(int original, @Local(argsOnly = true) BlockState state) {
        return TinkerKit.getFlags(state, original);
    }

    /**
     * This needs to be different because of carpet's modified quasi-connectivity logic.
     */
    @SuppressWarnings("UnresolvedMixinReference")
    @TargetHandler(mixin = "carpet.mixins.DispenserBlock_qcMixin", name = "carpet_hasQuasiSignal")
    @WrapOperation(method = "@MixinSquared:Handler", at = @At(value = "INVOKE", target =  "Lcarpet/helpers/QuasiConnectivity;hasQuasiSignal(Lnet/minecraft/world/RedstoneView;Lnet/minecraft/util/math/BlockPos;)Z"))
    private boolean shouldQC(RedstoneView instance, BlockPos pos, Operation<Boolean> original) {
        BlockState blockState = instance.getBlockState(pos);

        if (!TinkerKit.Type.QC.canModify(blockState.getBlock()))
            return original.call(instance, pos);

        int qcValue = (int) TinkerKit.Type.QC.getModifiedValue(blockState.getBlock());

        if (qcValue < 1)
            return false;

        for (int i = 1; i <= qcValue; i++) {
            BlockPos blockPos = pos.up(i);

            if (TinkerKit.cannotQC(instance, blockPos))
                break;

            if (instance.isReceivingRedstonePower(blockPos))
                return true;
        }

        return false;
    }

}
