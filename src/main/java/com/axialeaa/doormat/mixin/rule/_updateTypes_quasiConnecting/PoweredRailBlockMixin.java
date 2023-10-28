package com.axialeaa.doormat.mixin.rule._updateTypes_quasiConnecting;

import com.axialeaa.doormat.DoormatSettings;
import com.axialeaa.doormat.helpers.ConditionalRedstoneBehavior;
import com.llamalad7.mixinextras.injector.WrapWithCondition;
import net.minecraft.block.Block;
import net.minecraft.block.PoweredRailBlock;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyArg;
import org.spongepowered.asm.mixin.injection.Redirect;

@Mixin(PoweredRailBlock.class)
public class PoweredRailBlockMixin {

    @ModifyArg(method = "updateBlockState", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/World;setBlockState(Lnet/minecraft/util/math/BlockPos;Lnet/minecraft/block/BlockState;I)Z"))
    private int changeUpdateType(int flags) {
        return DoormatSettings.railUpdateType.getFlags();
    }

    @SuppressWarnings("unused") // otherwise it throws an error for every unused parameter
    @WrapWithCondition(method = "updateBlockState", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/World;updateNeighborsAlways(Lnet/minecraft/util/math/BlockPos;Lnet/minecraft/block/Block;)V", ordinal = 0))
    private boolean disableNeighborUpdatesOn(World world, BlockPos pos, Block block) {
        return ConditionalRedstoneBehavior.neighborUpdateOn(DoormatSettings.railUpdateType);
    }

    @Redirect(method = "isPoweredByOtherRails(Lnet/minecraft/world/World;Lnet/minecraft/util/math/BlockPos;ZILnet/minecraft/block/enums/RailShape;)Z", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/World;isReceivingRedstonePower(Lnet/minecraft/util/math/BlockPos;)Z"))
    private boolean allowQuasiConnectingA(World world, BlockPos pos) {
        return ConditionalRedstoneBehavior.quasiConnectOn(DoormatSettings.railQuasiConnecting, world, pos);
    }

    @Redirect(method = "updateBlockState", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/World;isReceivingRedstonePower(Lnet/minecraft/util/math/BlockPos;)Z"))
    private boolean allowQuasiConnectingB(World world, BlockPos pos) {
        return ConditionalRedstoneBehavior.quasiConnectOn(DoormatSettings.railQuasiConnecting, world, pos);
    }

}
