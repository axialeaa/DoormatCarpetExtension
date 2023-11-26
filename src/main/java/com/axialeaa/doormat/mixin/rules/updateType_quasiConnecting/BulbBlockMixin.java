package com.axialeaa.doormat.mixin.rules.updateType_quasiConnecting;

import com.axialeaa.doormat.DoormatSettings;
import com.axialeaa.doormat.helpers.RedstoneUpdateBehaviour;
import net.minecraft.block.BulbBlock;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.math.BlockPos;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyArg;
import org.spongepowered.asm.mixin.injection.Redirect;

@Mixin(BulbBlock.class)
public class BulbBlockMixin {

    @Redirect(method = "update", at = @At(value = "INVOKE", target = "Lnet/minecraft/server/world/ServerWorld;isReceivingRedstonePower(Lnet/minecraft/util/math/BlockPos;)Z"))
    private boolean allowQuasiConnecting(ServerWorld world, BlockPos pos) {
        return RedstoneUpdateBehaviour.quasiConnectOn(DoormatSettings.bulbQuasiConnecting, world, pos);
    }

    @ModifyArg(method = "update", at = @At(value = "INVOKE", target = "Lnet/minecraft/server/world/ServerWorld;setBlockState(Lnet/minecraft/util/math/BlockPos;Lnet/minecraft/block/BlockState;I)Z"))
    private int changeUpdateType_neighborUpdate(int flags) {
        return DoormatSettings.bulbUpdateType.getFlags();
    }

}
