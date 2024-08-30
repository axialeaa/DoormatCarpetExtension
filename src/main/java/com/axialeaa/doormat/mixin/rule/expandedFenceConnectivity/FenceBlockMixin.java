package com.axialeaa.doormat.mixin.rule.expandedFenceConnectivity;

import com.axialeaa.doormat.setting.DoormatSettings;
import com.llamalad7.mixinextras.injector.ModifyReturnValue;
import com.llamalad7.mixinextras.sugar.Local;
import net.minecraft.block.*;
import net.minecraft.registry.tag.BlockTags;
import net.minecraft.util.math.Direction;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

@Mixin(FenceBlock.class)
public class FenceBlockMixin {

    @ModifyReturnValue(method = "canConnect", at = @At("RETURN"))
    private boolean modifyCanConnect(boolean original, @Local(argsOnly = true) BlockState state, @Local(argsOnly = true) Direction dir) {
        return original || (DoormatSettings.expandedFenceConnectivity && (state.isIn(BlockTags.WALLS) || state.getBlock() instanceof PaneBlock));
    }

}
