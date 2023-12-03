package com.axialeaa.doormat.mixin.rules.updateType_quasiConnecting;

import com.axialeaa.doormat.DoormatSettings;
import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import net.minecraft.block.Block;
import net.minecraft.block.DoorBlock;
import net.minecraft.block.enums.DoubleBlockHalf;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyArg;

@Mixin(DoorBlock.class)
public class DoorBlockMixin {

    @ModifyArg(method = "onUse", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/World;setBlockState(Lnet/minecraft/util/math/BlockPos;Lnet/minecraft/block/BlockState;I)Z"))
    private int changeUpdateType_onUse(int flags) {
        return DoormatSettings.doorUpdateType.getFlags() | Block.REDRAW_ON_MAIN_THREAD;
    }

    @ModifyArg(method = "neighborUpdate", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/World;setBlockState(Lnet/minecraft/util/math/BlockPos;Lnet/minecraft/block/BlockState;I)Z"))
    private int changeUpdateType_neighborUpdate(int flags) {
        return DoormatSettings.doorUpdateType.getFlags();
    }

    @ModifyArg(method = "setOpen", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/World;setBlockState(Lnet/minecraft/util/math/BlockPos;Lnet/minecraft/block/BlockState;I)Z"))
    private int changeUpdateType_setOpen(int flags) {
        return DoormatSettings.doorUpdateType.getFlags() | Block.REDRAW_ON_MAIN_THREAD;
    }

    /**
     * This is weird because doors are two blocks tall, and both halves need to activate at the same time.
     */
    @WrapOperation(method = "neighborUpdate", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/World;isReceivingRedstonePower(Lnet/minecraft/util/math/BlockPos;)Z", ordinal = 0))
    private boolean allowQuasiConnecting(World world, BlockPos pos, Operation<Boolean> original) {
        return original.call(world, pos) || DoormatSettings.doorQuasiConnecting && world.isReceivingRedstonePower(pos.up(world.getBlockState(pos).get(DoorBlock.HALF) == DoubleBlockHalf.LOWER ? 2 : 1));
    }

}
