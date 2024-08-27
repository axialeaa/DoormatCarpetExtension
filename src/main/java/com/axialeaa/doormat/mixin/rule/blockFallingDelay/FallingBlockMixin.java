package com.axialeaa.doormat.mixin.rule.blockFallingDelay;

import com.axialeaa.doormat.settings.DoormatSettings;
import com.llamalad7.mixinextras.injector.ModifyReturnValue;
import net.minecraft.block.FallingBlock;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

@Mixin(FallingBlock.class)
public class FallingBlockMixin {
    
    @ModifyReturnValue(method = "getFallDelay", at = @At("RETURN"))
    private int modifyFallDelay(int original) {
        return DoormatSettings.blockFallingDelay;
    }
    
}
