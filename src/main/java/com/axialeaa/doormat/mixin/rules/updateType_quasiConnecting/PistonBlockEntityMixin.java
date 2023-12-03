package com.axialeaa.doormat.mixin.rules.updateType_quasiConnecting;

import com.axialeaa.doormat.DoormatSettings;
import com.axialeaa.doormat.helpers.RedstoneHelper;
import com.llamalad7.mixinextras.injector.WrapWithCondition;
import net.minecraft.block.Block;
import net.minecraft.block.entity.PistonBlockEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyArg;

@Mixin(PistonBlockEntity.class)
public class PistonBlockEntityMixin {

    @ModifyArg(method = "finish", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/World;setBlockState(Lnet/minecraft/util/math/BlockPos;Lnet/minecraft/block/BlockState;I)Z"))
    private int changeUpdateType_finish(int flags) {
        return DoormatSettings.pistonUpdateType.getFlags();
    }

    @ModifyArg(method = "tick", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/World;setBlockState(Lnet/minecraft/util/math/BlockPos;Lnet/minecraft/block/BlockState;I)Z", ordinal = 1))
    private static int changeUpdateType_tick(int flags) {
        return DoormatSettings.pistonUpdateType.getFlags() | Block.MOVED;
    }

    @WrapWithCondition(method = "finish", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/World;updateNeighbor(Lnet/minecraft/util/math/BlockPos;Lnet/minecraft/block/Block;Lnet/minecraft/util/math/BlockPos;)V", ordinal = 0))
    private boolean disableNeighborUpdates_finish(World world, BlockPos pos, Block sourceBlock, BlockPos sourcePos) {
        return RedstoneHelper.neighbourUpdateForRule(DoormatSettings.pistonUpdateType);
    }

    @WrapWithCondition(method = "tick", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/World;updateNeighbor(Lnet/minecraft/util/math/BlockPos;Lnet/minecraft/block/Block;Lnet/minecraft/util/math/BlockPos;)V", ordinal = 0))
    private static boolean disableNeighborUpdates_tick(World world, BlockPos pos, Block sourceBlock, BlockPos sourcePos) {
        return RedstoneHelper.neighbourUpdateForRule(DoormatSettings.pistonUpdateType);
    }

}
