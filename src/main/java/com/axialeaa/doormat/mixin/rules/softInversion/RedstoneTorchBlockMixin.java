package com.axialeaa.doormat.mixin.rules.softInversion;

import com.axialeaa.doormat.DoormatSettings;
import com.llamalad7.mixinextras.injector.ModifyReturnValue;
import net.minecraft.block.BlockState;
import net.minecraft.block.PistonBlock;
import net.minecraft.block.RedstoneTorchBlock;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

@Mixin(RedstoneTorchBlock.class)
public class RedstoneTorchBlockMixin {

    @ModifyReturnValue(method = "shouldUnpower", at = @At("RETURN"))
    private boolean unpowerOnPistonExtend(boolean original, World world, BlockPos pos) {
        BlockState blockState = world.getBlockState(pos.down());
        return DoormatSettings.softInversion && blockState.getBlock() instanceof PistonBlock ?
            blockState.get(PistonBlock.EXTENDED) : original;
    }

}
