package com.axialeaa.doormat.mixin.tinker_kit;

import com.axialeaa.doormat.helper.SoftInversionHelper;
import com.axialeaa.doormat.tinker_kit.TinkerKitUtils;
import com.llamalad7.mixinextras.injector.ModifyReturnValue;
import com.llamalad7.mixinextras.sugar.Local;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.WallRedstoneTorchBlock;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

@Mixin(value = WallRedstoneTorchBlock.class, priority = 1500)
public class WallRedstoneTorchBlockMixin {

    @ModifyReturnValue(method = "shouldUnpower", at = @At("RETURN"))
    private boolean allowQuasiConnectivity(boolean original, World world, BlockPos pos, BlockState state, @Local Direction direction) {
        Block block = state.getBlock();
        BlockPos blockPos = pos.offset(direction);

        return TinkerKitUtils.isEmittingRedstonePower(world, blockPos, block, direction) || SoftInversionHelper.isPistonExtended(world, blockPos);
    }

}
