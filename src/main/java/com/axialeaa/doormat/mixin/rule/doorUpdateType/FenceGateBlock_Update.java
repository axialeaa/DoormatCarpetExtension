package com.axialeaa.doormat.mixin.rule.doorUpdateType;

import com.axialeaa.doormat.DoormatSettings;
import net.minecraft.block.Block;
import net.minecraft.block.FenceGateBlock;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyArg;

@Mixin(FenceGateBlock.class)
public class FenceGateBlock_Update {

    @ModifyArg(method = "neighborUpdate", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/World;setBlockState(Lnet/minecraft/util/math/BlockPos;Lnet/minecraft/block/BlockState;I)Z"))
    private int changeNeighborUpdate(int flags) {
        return DoormatSettings.doorUpdateType.getFlags();
    }

    @ModifyArg(method = "onUse", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/World;setBlockState(Lnet/minecraft/util/math/BlockPos;Lnet/minecraft/block/BlockState;I)Z"))
    private int changeOnUseUpdate(int flags) {
        return DoormatSettings.doorUpdateType.getFlags() | Block.REDRAW_ON_MAIN_THREAD;
    }

}
