package com.axialeaa.doormat.mixin.rule.bellUpdateType;

import com.axialeaa.doormat.DoormatSettings;
import net.minecraft.block.BellBlock;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyArg;

@Mixin(BellBlock.class)
public class BellBlockMixin_Update {

    @ModifyArg(method = "neighborUpdate", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/World;setBlockState(Lnet/minecraft/util/math/BlockPos;Lnet/minecraft/block/BlockState;I)Z"))
    private int changeNeighborUpdate(int flags) {
        return DoormatSettings.bellUpdateType.getFlags();
    }

}
