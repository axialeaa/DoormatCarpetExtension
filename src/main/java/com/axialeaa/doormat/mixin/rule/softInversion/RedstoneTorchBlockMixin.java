package com.axialeaa.doormat.mixin.rule.softInversion;

import com.axialeaa.doormat.setting.DoormatSettings;
import com.axialeaa.doormat.helper.SoftInversionHelper;
import com.llamalad7.mixinextras.injector.ModifyReturnValue;
import net.minecraft.block.RedstoneTorchBlock;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

@Mixin(RedstoneTorchBlock.class)
public class RedstoneTorchBlockMixin {

    @ModifyReturnValue(method = "shouldUnpower", at = @At("RETURN"))
    private boolean unpowerOnPistonExtend(boolean original, World world, BlockPos pos) {
        return original || DoormatSettings.softInversion && SoftInversionHelper.isPistonExtended(world, pos.down());
    }

}
