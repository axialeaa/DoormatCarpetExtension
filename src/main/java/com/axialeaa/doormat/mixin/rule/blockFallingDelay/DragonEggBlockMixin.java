package com.axialeaa.doormat.mixin.rule.blockFallingDelay;

import com.axialeaa.doormat.DoormatSettings;
import com.llamalad7.mixinextras.injector.ModifyReturnValue;
import net.minecraft.block.DragonEggBlock;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

@Mixin(DragonEggBlock.class)
public class DragonEggBlockMixin {
    
    @ModifyReturnValue(method = "getFallDelay", at = @At("RETURN"))
    private int modifyFallDelay(int original) {
        return DoormatSettings.dragonEggFallingDelay;
    }
    
}
