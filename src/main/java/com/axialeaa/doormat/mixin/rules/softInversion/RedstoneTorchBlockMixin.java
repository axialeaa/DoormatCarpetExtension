package com.axialeaa.doormat.mixin.rules.softInversion;

import com.axialeaa.doormat.helpers.RedstoneUpdateBehaviour;
import com.llamalad7.mixinextras.injector.ModifyReturnValue;
import net.minecraft.block.RedstoneTorchBlock;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

@Mixin(RedstoneTorchBlock.class)
public class RedstoneTorchBlockMixin {

    @ModifyReturnValue(method = "shouldUnpower", at = @At("RETURN"))
    private boolean unpowerOnPistonExtend(boolean original, World world, BlockPos pos) {
        return RedstoneUpdateBehaviour.unpowerOnPistonExtended(original, world, pos, Direction.DOWN);
    }

}
