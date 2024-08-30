package com.axialeaa.doormat.mixin.rule.softInversion;

import com.axialeaa.doormat.setting.DoormatSettings;
import com.axialeaa.doormat.helper.SoftInversionHelper;
import com.llamalad7.mixinextras.injector.ModifyReturnValue;
import com.llamalad7.mixinextras.sugar.Local;
import net.minecraft.block.WallRedstoneTorchBlock;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

@Mixin(WallRedstoneTorchBlock.class)
public class WallRedstoneTorchBlockMixin {

    @ModifyReturnValue(method = "shouldUnpower", at = @At("RETURN"))
    private boolean unpowerOnPistonExtend(boolean original, World world, BlockPos pos, @Local Direction direction) {
        return original || DoormatSettings.softInversion && SoftInversionHelper.isPistonExtended(world, pos.offset(direction));
    }

}
