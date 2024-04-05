package com.axialeaa.doormat.mixin.rule.disableCrossing;

import com.axialeaa.doormat.DoormatSettings;
import com.llamalad7.mixinextras.injector.ModifyReturnValue;
import net.minecraft.block.entity.EndGatewayBlockEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

@Mixin(EndGatewayBlockEntity.class)
public class EndGatewayBlockEntityMixin {

    @ModifyReturnValue(method = "canTeleport", at = @At("RETURN"))
    private static boolean shouldTeleport(boolean original) {
        return original && !DoormatSettings.disableEndGatewayCrossing;
    }

}
