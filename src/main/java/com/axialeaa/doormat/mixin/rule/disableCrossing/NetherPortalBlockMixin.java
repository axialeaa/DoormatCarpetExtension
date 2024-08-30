package com.axialeaa.doormat.mixin.rule.disableCrossing;

import com.axialeaa.doormat.setting.DoormatSettings;
import com.llamalad7.mixinextras.injector.ModifyExpressionValue;
import net.minecraft.block.NetherPortalBlock;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

@Mixin(NetherPortalBlock.class)
public class NetherPortalBlockMixin {

    @ModifyExpressionValue(method = "onEntityCollision", at = @At(value = "INVOKE", target = "Lnet/minecraft/entity/Entity;canUsePortals(Z)Z"))
    private boolean shouldTeleport(boolean original) {
        return original && !DoormatSettings.disableNetherPortalCrossing;
    }

}
