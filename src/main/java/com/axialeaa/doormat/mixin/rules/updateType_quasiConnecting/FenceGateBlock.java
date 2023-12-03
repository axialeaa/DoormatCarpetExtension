package com.axialeaa.doormat.mixin.rules.updateType_quasiConnecting;

import com.axialeaa.doormat.DoormatSettings;
import com.axialeaa.doormat.helpers.RedstoneHelper;
import net.minecraft.block.Block;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyArg;
import org.spongepowered.asm.mixin.injection.Redirect;

@Mixin(net.minecraft.block.FenceGateBlock.class)
public class FenceGateBlock {

    @ModifyArg(method = { "neighborUpdate", "onUse" }, at = @At(value = "INVOKE", target = "Lnet/minecraft/world/World;setBlockState(Lnet/minecraft/util/math/BlockPos;Lnet/minecraft/block/BlockState;I)Z"))
    private int changeUpdateType(int flags) {
        return DoormatSettings.fenceGateUpdateType.getFlags() | Block.REDRAW_ON_MAIN_THREAD;
    }

    @Redirect(method = "neighborUpdate", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/World;isReceivingRedstonePower(Lnet/minecraft/util/math/BlockPos;)Z"))
    private boolean allowQuasiConnecting(World world, BlockPos pos) {
        return RedstoneHelper.quasiConnectForRule(world, pos, DoormatSettings.fenceGateQuasiConnecting);
    }

}
