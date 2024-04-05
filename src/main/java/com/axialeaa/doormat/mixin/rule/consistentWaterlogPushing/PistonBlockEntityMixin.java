package com.axialeaa.doormat.mixin.rule.consistentWaterlogPushing;

import com.axialeaa.doormat.DoormatSettings;
import com.llamalad7.mixinextras.injector.ModifyExpressionValue;
import net.minecraft.block.entity.PistonBlockEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

@Mixin(PistonBlockEntity.class)
public class PistonBlockEntityMixin {
    
    @ModifyExpressionValue(method = "tick", at = @At(value = "INVOKE", target = "Lnet/minecraft/block/BlockState;contains(Lnet/minecraft/state/property/Property;)Z"))
    private static boolean bypassWaterlogRemoval(boolean original) {
        return !DoormatSettings.consistentWaterlogPushing && original;
    }
    
}
