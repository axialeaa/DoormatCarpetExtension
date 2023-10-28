package com.axialeaa.doormat.mixin.rule._updateTypes_quasiConnecting;

import com.axialeaa.doormat.DoormatSettings;
import com.axialeaa.doormat.helpers.ConditionalRedstoneBehavior;
import com.llamalad7.mixinextras.injector.WrapWithCondition;
import net.minecraft.block.Block;
import net.minecraft.block.PistonBlock;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyArg;

@Mixin(PistonBlock.class)
public class PistonBlockMixin {

    @ModifyArg(method = "onSyncedBlockEvent", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/World;setBlockState(Lnet/minecraft/util/math/BlockPos;Lnet/minecraft/block/BlockState;I)Z", ordinal = 1))
    private int changeNeighborUpdate(int flags) {
        return DoormatSettings.pistonUpdateType.getFlags() | Block.MOVED;
    }

    @SuppressWarnings("unused") // otherwise it throws an error for every unused parameter
    @WrapWithCondition(method = "onSyncedBlockEvent", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/World;updateNeighbors(Lnet/minecraft/util/math/BlockPos;Lnet/minecraft/block/Block;)V", ordinal = 0))
    private boolean changeNeighborUpdate(World world, BlockPos pos, Block block) {
        return ConditionalRedstoneBehavior.neighborUpdateOn(DoormatSettings.pistonUpdateType);
    }

    @SuppressWarnings("unused") // otherwise it throws an error for every unused parameter
    @WrapWithCondition(method = "move", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/World;updateNeighborsAlways(Lnet/minecraft/util/math/BlockPos;Lnet/minecraft/block/Block;)V"))
    private boolean test(World world, BlockPos pos, Block block) {
        return ConditionalRedstoneBehavior.neighborUpdateOn(DoormatSettings.pistonUpdateType);
    }

}
