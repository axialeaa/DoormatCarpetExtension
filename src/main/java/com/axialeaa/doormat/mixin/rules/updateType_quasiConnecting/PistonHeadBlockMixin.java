package com.axialeaa.doormat.mixin.rules.updateType_quasiConnecting;

import com.axialeaa.doormat.DoormatSettings;
import com.axialeaa.doormat.helpers.RedstoneHelper;
import com.llamalad7.mixinextras.injector.ModifyExpressionValue;
import net.minecraft.block.PistonHeadBlock;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

@Mixin(PistonHeadBlock.class)
public class PistonHeadBlockMixin {

    @ModifyExpressionValue(method = "neighborUpdate", at = @At(value = "INVOKE", target = "Lnet/minecraft/block/BlockState;canPlaceAt(Lnet/minecraft/world/WorldView;Lnet/minecraft/util/math/BlockPos;)Z"))
    private boolean disableNeighborUpdates(boolean original) {
        return original && RedstoneHelper.neighbourUpdateForRule(DoormatSettings.pistonUpdateType);
    }

}
