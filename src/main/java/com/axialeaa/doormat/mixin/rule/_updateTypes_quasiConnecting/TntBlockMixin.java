package com.axialeaa.doormat.mixin.rule._updateTypes_quasiConnecting;

import com.axialeaa.doormat.DoormatSettings;
import com.axialeaa.doormat.helpers.UpdateBehaviorHelper;
import net.minecraft.block.Block;
import net.minecraft.block.Blocks;
import net.minecraft.block.TntBlock;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyArg;
import org.spongepowered.asm.mixin.injection.Redirect;

@Mixin(TntBlock.class)
public class TntBlockMixin {

    @Redirect(method = { "onBlockAdded", "neighborUpdate", "onProjectileHit" }, at = @At(value = "INVOKE", target = "Lnet/minecraft/world/World;removeBlock(Lnet/minecraft/util/math/BlockPos;Z)Z"))
    private boolean changeUpdateType(World world, BlockPos pos, boolean move) {
        return world.setBlockState(pos, Blocks.AIR.getDefaultState(), DoormatSettings.tntUpdateType.getFlags());
        // reconstructing the function of removeBlock() is necessary here, because neighbor updates are intrinsic to that method
    }

    @ModifyArg(method = "onUse", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/World;setBlockState(Lnet/minecraft/util/math/BlockPos;Lnet/minecraft/block/BlockState;I)Z"))
    private int changeUpdateType_onUse(int flags) {
        return DoormatSettings.tntUpdateType.getFlags() | Block.REDRAW_ON_MAIN_THREAD;
    }

    @Redirect(method = "neighborUpdate", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/World;isReceivingRedstonePower(Lnet/minecraft/util/math/BlockPos;)Z"))
    private boolean allowQuasiConnecting(World world, BlockPos pos) {
        return UpdateBehaviorHelper.quasiConnectOn(DoormatSettings.tntQuasiConnecting, world, pos);
    }


}
