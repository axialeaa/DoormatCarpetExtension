package com.axialeaa.doormat.mixin.block.update;

import com.axialeaa.doormat.DoormatSettings;
import net.minecraft.block.RedstoneLampBlock;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyArg;

@Mixin(RedstoneLampBlock.class)
public class RedstoneLampBlockMixin_Update {

    @ModifyArg(method = "neighborUpdate", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/World;setBlockState(Lnet/minecraft/util/math/BlockPos;Lnet/minecraft/block/BlockState;I)Z"))
    private int changeNeighborUpdate(int flags) {
        return DoormatSettings.lampUpdateType.getFlags();
    }

    @ModifyArg(method = "scheduledTick", at = @At(value = "INVOKE", target = "Lnet/minecraft/server/world/ServerWorld;setBlockState(Lnet/minecraft/util/math/BlockPos;Lnet/minecraft/block/BlockState;I)Z"))
    private int changeScheduledTickUpdate(int flags) {
        return DoormatSettings.lampUpdateType.getFlags();
    }

}
