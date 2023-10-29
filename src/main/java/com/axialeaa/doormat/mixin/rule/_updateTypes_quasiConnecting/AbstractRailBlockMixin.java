package com.axialeaa.doormat.mixin.rule._updateTypes_quasiConnecting;

import com.axialeaa.doormat.DoormatSettings;
import com.axialeaa.doormat.helpers.ConditionalRedstoneBehavior;
import com.llamalad7.mixinextras.injector.WrapWithCondition;
import net.minecraft.block.AbstractRailBlock;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

@Mixin(AbstractRailBlock.class)
public class AbstractRailBlockMixin {

    @SuppressWarnings("unused") // otherwise it throws an error for each unused parameter
    @WrapWithCondition(method = "updateCurves", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/World;updateNeighbor(Lnet/minecraft/block/BlockState;Lnet/minecraft/util/math/BlockPos;Lnet/minecraft/block/Block;Lnet/minecraft/util/math/BlockPos;Z)V"))
    private boolean disableNeighborUpdates_updateCurves(World world, BlockState state, BlockPos pos, Block sourceBlock, BlockPos sourcePos, boolean notify) {
        return ConditionalRedstoneBehavior.neighborUpdateOn(DoormatSettings.railUpdateType);
    }

    @SuppressWarnings("unused") // otherwise it throws an error for each unused parameter
    @WrapWithCondition(method = "onStateReplaced", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/World;updateNeighborsAlways(Lnet/minecraft/util/math/BlockPos;Lnet/minecraft/block/Block;)V"))
    private boolean disableNeighborUpdates_onStateReplaced(World world, BlockPos pos, Block sourceBlock) {
        return ConditionalRedstoneBehavior.neighborUpdateOn(DoormatSettings.railUpdateType);
    }

    @Redirect(method = "updateBlockState(Lnet/minecraft/world/World;Lnet/minecraft/util/math/BlockPos;Lnet/minecraft/block/BlockState;Z)Lnet/minecraft/block/BlockState;", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/World;isReceivingRedstonePower(Lnet/minecraft/util/math/BlockPos;)Z"))
    private boolean allowQuasiConnecting(World world, BlockPos pos) {
        return ConditionalRedstoneBehavior.quasiConnectOn(DoormatSettings.railQuasiConnecting, world, pos);
    }

}
