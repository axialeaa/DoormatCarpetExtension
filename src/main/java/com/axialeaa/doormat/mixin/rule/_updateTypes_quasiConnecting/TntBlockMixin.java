package com.axialeaa.doormat.mixin.rule._updateTypes_quasiConnecting;

import com.axialeaa.doormat.DoormatSettings;
import com.axialeaa.doormat.helpers.ConditionalRedstoneBehavior;
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

    @Redirect(method = "onBlockAdded", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/World;removeBlock(Lnet/minecraft/util/math/BlockPos;Z)Z"))
    private boolean changeOnBlockAdded(World world, BlockPos pos, boolean move) {
        return world.setBlockState(pos, Blocks.AIR.getDefaultState(), DoormatSettings.tntUpdateType.getFlags());
    }

    @Redirect(method = "neighborUpdate", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/World;removeBlock(Lnet/minecraft/util/math/BlockPos;Z)Z"))
    private boolean changeNeighborUpdate(World world, BlockPos pos, boolean move) {
        return world.setBlockState(pos, Blocks.AIR.getDefaultState(), DoormatSettings.tntUpdateType.getFlags());
    }

    @ModifyArg(method = "onUse", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/World;setBlockState(Lnet/minecraft/util/math/BlockPos;Lnet/minecraft/block/BlockState;I)Z"))
    private int changeOnUse(int flags) {
        return DoormatSettings.tntUpdateType.getFlags() | Block.REDRAW_ON_MAIN_THREAD;
    }

    @Redirect(method = "onProjectileHit", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/World;removeBlock(Lnet/minecraft/util/math/BlockPos;Z)Z"))
    private boolean changeOnUse(World world, BlockPos pos, boolean move) {
        return world.setBlockState(pos, Blocks.AIR.getDefaultState(), DoormatSettings.tntUpdateType.getFlags());
    }

    @Redirect(method = "neighborUpdate", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/World;isReceivingRedstonePower(Lnet/minecraft/util/math/BlockPos;)Z"))
    private boolean allowQuasiConnecting(World world, BlockPos pos) {
        return ConditionalRedstoneBehavior.quasiConnectOnCondition(DoormatSettings.tntQuasiConnecting, world, pos);
    }


}
