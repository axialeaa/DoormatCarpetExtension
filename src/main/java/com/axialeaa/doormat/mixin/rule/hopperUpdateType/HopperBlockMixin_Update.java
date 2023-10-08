package com.axialeaa.doormat.mixin.rule.hopperUpdateType;

import com.axialeaa.doormat.DoormatSettings;
import net.minecraft.block.HopperBlock;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyArg;

@Mixin(HopperBlock.class)
public class HopperBlockMixin_Update {

    @ModifyArg(method = "updateEnabled", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/World;setBlockState(Lnet/minecraft/util/math/BlockPos;Lnet/minecraft/block/BlockState;I)Z"))
    private int changeNeighborUpdate(int flags) {
        return DoormatSettings.hopperUpdateType.getFlags();
    }

}
