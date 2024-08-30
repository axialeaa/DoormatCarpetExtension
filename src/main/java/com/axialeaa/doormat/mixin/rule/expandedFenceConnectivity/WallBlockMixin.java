package com.axialeaa.doormat.mixin.rule.expandedFenceConnectivity;

import com.axialeaa.doormat.setting.DoormatSettings;
import com.llamalad7.mixinextras.injector.ModifyReturnValue;
import com.llamalad7.mixinextras.sugar.Local;
import net.minecraft.block.*;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

@Mixin(WallBlock.class)
public class WallBlockMixin {

    @ModifyReturnValue(method = "shouldConnectTo", at = @At("RETURN"))
    private boolean modifyShouldConnectTo(boolean original, @Local(argsOnly = true) BlockState state) {
        return original || (DoormatSettings.expandedFenceConnectivity && state.getBlock() instanceof FenceBlock);
    }

}
