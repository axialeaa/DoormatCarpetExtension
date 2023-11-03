package com.axialeaa.doormat.mixin.rule._updateTypes_quasiConnecting;

import com.axialeaa.doormat.DoormatSettings;
import com.axialeaa.doormat.helpers.UpdateBehaviorHelper;
import com.llamalad7.mixinextras.injector.ModifyExpressionValue;
import net.minecraft.block.BlockState;
import net.minecraft.block.PistonHeadBlock;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

@Mixin(PistonHeadBlock.class)
public class PistonHeadBlockMixin {

    @SuppressWarnings("unused") // otherwise it throws an error for every unused parameter
    @ModifyExpressionValue(method = "neighborUpdate", at = @At(value = "INVOKE", target = "Lnet/minecraft/block/BlockState;canPlaceAt(Lnet/minecraft/world/WorldView;Lnet/minecraft/util/math/BlockPos;)Z"))
    private boolean disableNeighborUpdates(boolean original, BlockState state, World world, BlockPos pos) {
        return original && UpdateBehaviorHelper.neighborUpdateOn(DoormatSettings.pistonUpdateType);
    }

}
