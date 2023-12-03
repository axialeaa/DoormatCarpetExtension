package com.axialeaa.doormat.mixin.rules.updateType_quasiConnecting;

import com.axialeaa.doormat.DoormatSettings;
import com.axialeaa.doormat.helpers.RedstoneHelper;
import com.bawnorton.mixinsquared.TargetHandler;
import com.llamalad7.mixinextras.injector.WrapWithCondition;
import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import net.minecraft.block.Block;
import net.minecraft.block.PistonBlock;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.RedstoneView;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyArg;

@Mixin(value = PistonBlock.class, priority = 1500)
public class PistonBlockMixin {

    @ModifyArg(method = "onSyncedBlockEvent", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/World;setBlockState(Lnet/minecraft/util/math/BlockPos;Lnet/minecraft/block/BlockState;I)Z", ordinal = 1))
    private int changeUpdateType(int flags) {
        return DoormatSettings.pistonUpdateType.getFlags() | Block.MOVED;
    }

    @WrapWithCondition(method = "onSyncedBlockEvent", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/World;updateNeighbors(Lnet/minecraft/util/math/BlockPos;Lnet/minecraft/block/Block;)V", ordinal = 0))
    private boolean disableNeighborUpdates_onSyncedBlockEvent(World world, BlockPos pos, Block block) {
        return RedstoneHelper.neighbourUpdateForRule(DoormatSettings.pistonUpdateType);
    }

    @WrapWithCondition(method = "move", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/World;updateNeighborsAlways(Lnet/minecraft/util/math/BlockPos;Lnet/minecraft/block/Block;)V"))
    private boolean disableNeighborUpdates_move(World world, BlockPos pos, Block block) {
        return RedstoneHelper.neighbourUpdateForRule(DoormatSettings.pistonUpdateType);
    }

    /**
     * This needs to be different because of carpet's modified quasi-connectivity logic.
     */
    @TargetHandler(mixin = "carpet.mixins.PistonBaseBlock_qcMixin", name = "carpet_checkQuasiSignal")
    @WrapOperation(method = "@MixinSquared:Handler", at = @At(value = "INVOKE", target =  "Lcarpet/helpers/QuasiConnectivity;hasQuasiSignal(Lnet/minecraft/world/RedstoneView;Lnet/minecraft/util/math/BlockPos;)Z"))
    private boolean disableCarpetQC(RedstoneView world, BlockPos pos, Operation<Boolean> original) {
        return original.call(world, pos) && DoormatSettings.pistonQuasiConnecting;
    }

}
