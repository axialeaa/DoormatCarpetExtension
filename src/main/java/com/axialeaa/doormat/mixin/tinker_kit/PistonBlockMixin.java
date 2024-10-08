package com.axialeaa.doormat.mixin.tinker_kit;

import com.axialeaa.doormat.mixin.impl.AbstractBlockImplMixin;
import com.axialeaa.doormat.registry.DoormatTinkerTypes;
import com.axialeaa.doormat.tinker_kit.TinkerKitUtils;
import com.bawnorton.mixinsquared.TargetHandler;
import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import com.llamalad7.mixinextras.sugar.Local;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.PistonBlock;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.random.Random;
import net.minecraft.world.RedstoneView;
import net.minecraft.world.World;
import net.minecraft.world.tick.TickPriority;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;

@Mixin(value = PistonBlock.class, priority = 1500)
public class PistonBlockMixin extends AbstractBlockImplMixin {

    @Unique private int type;
    @Unique private int data;

    /**
     * This needs to be different because of carpet's modified quasi-connectivity logic.
     */
    @SuppressWarnings("UnresolvedMixinReference")
    @TargetHandler(mixin = "carpet.mixins.PistonBaseBlock_qcMixin", name = "carpet_checkQuasiSignal")
    @WrapOperation(method = "@MixinSquared:Handler", at = @At(value = "INVOKE", target =  "Lcarpet/helpers/QuasiConnectivity;hasQuasiSignal(Lnet/minecraft/world/RedstoneView;Lnet/minecraft/util/math/BlockPos;)Z"))
    private boolean shouldQC(RedstoneView instance, BlockPos pos, Operation<Boolean> original) {
        BlockState blockState = instance.getBlockState(pos);
        Block block = blockState.getBlock();

        if (!DoormatTinkerTypes.QC.canModify(block))
            return original.call(instance, pos);

        var value = DoormatTinkerTypes.QC.getValue(block);

        if (value == null || value < 1)
            return false;

        for (int i = 1; i <= value; i++) {
            BlockPos blockPos = pos.up(i);

            if (TinkerKitUtils.cannotQC(instance, blockPos))
                break;

            if (instance.isReceivingRedstonePower(blockPos))
                return true;
        }

        return false;
    }

    @WrapOperation(method = "tryMove", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/World;addSyncedBlockEvent(Lnet/minecraft/util/math/BlockPos;Lnet/minecraft/block/Block;II)V"))
    private void scheduleOrCall(World instance, BlockPos pos, Block block, int type, int data, Operation<Void> original, @Local(argsOnly = true) BlockState state) {
        int delay = TinkerKitUtils.getDelay(block, 0);

        if (delay > 0) {
            this.type = type;
            this.data = data;

            TickPriority tickPriority = TinkerKitUtils.getTickPriority(block);
            instance.scheduleBlockTick(pos, block, delay, tickPriority);
        }
        else original.call(instance, pos, block, type, data);
    }

    @Override
    public void scheduledTickImpl(BlockState state, ServerWorld world, BlockPos pos, Random random, Operation<Void> original) {
        Block block = state.getBlock();
        world.addSyncedBlockEvent(pos, block, type, data);
        original.call(state, world, pos, random);
    }

}
