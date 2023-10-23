package com.axialeaa.doormat.mixin.rule._updateTypes_quasiConnecting;

import com.axialeaa.doormat.DoormatSettings;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.PistonBlock;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyArg;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.Slice;

@Mixin(PistonBlock.class)
public class PistonBlockMixin {

    @ModifyArg(method = "onSyncedBlockEvent", slice = @Slice(from = @At(value = "INVOKE", target = "Lnet/minecraft/block/PistonBlock;move(Lnet/minecraft/world/World;Lnet/minecraft/util/math/BlockPos;Lnet/minecraft/util/math/Direction;Z)Z")), at = @At(value = "INVOKE", target = "Lnet/minecraft/world/World;setBlockState(Lnet/minecraft/util/math/BlockPos;Lnet/minecraft/block/BlockState;I)Z"))
    private int disableQuasiConnecting(int flags) {
        return DoormatSettings.pistonUpdateType.getFlags() | Block.MOVED;
    }

    @Redirect(method = "shouldExtend", at = @At(value = "INVOKE", target = "Lnet/minecraft/util/math/BlockPos;up()Lnet/minecraft/util/math/BlockPos;"))
    private BlockPos disableQuasiConnecting(BlockPos pos) {
        return DoormatSettings.pistonQuasiConnecting ? pos.up() : pos;
    }

}
