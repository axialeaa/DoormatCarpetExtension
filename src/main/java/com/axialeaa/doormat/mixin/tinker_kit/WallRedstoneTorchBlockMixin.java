package com.axialeaa.doormat.mixin.tinker_kit;

import com.axialeaa.doormat.tinker_kit.TinkerKit;
import com.llamalad7.mixinextras.injector.ModifyReturnValue;
import com.llamalad7.mixinextras.sugar.Local;
import net.minecraft.block.BlockState;
import net.minecraft.block.WallRedstoneTorchBlock;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

@Mixin(WallRedstoneTorchBlock.class)
public class WallRedstoneTorchBlockMixin {

    @ModifyReturnValue(method = "shouldUnpower", at = @At("RETURN"))
    private boolean allowQuasiConnecting(boolean original, World world, BlockPos pos, BlockState state, @Local Direction direction) {
        return TinkerKit.isReceivingRedstonePower(world, pos, state, direction);
    }

}
