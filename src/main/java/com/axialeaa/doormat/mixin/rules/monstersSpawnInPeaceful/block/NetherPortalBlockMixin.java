package com.axialeaa.doormat.mixin.rules.monstersSpawnInPeaceful.block;

import com.axialeaa.doormat.DoormatSettings;
import com.llamalad7.mixinextras.injector.ModifyExpressionValue;
import net.minecraft.block.NetherPortalBlock;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

@Mixin(NetherPortalBlock.class)
public class NetherPortalBlockMixin {

    /**
     * @return the normal return value of this invocation if the rule is disabled, otherwise the difficulty ID or 1, whichever is largest.
     * This essentially stops it ever reaching 0 and disabling peaceful mode behaviour.
     */
    @ModifyExpressionValue(method = "randomTick", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/Difficulty;getId()I"))
    private int capAbovePeacefulId(int original) {
        return DoormatSettings.monstersSpawnInPeaceful.isEnabled() ? Math.max(original, 1) : original;
    }

}
