package com.axialeaa.doormat.mixin.rule.peacefulMonsterSpawning.world;

import com.axialeaa.doormat.setting.DoormatSettings;
import com.llamalad7.mixinextras.injector.ModifyReturnValue;
import net.minecraft.entity.SpawnGroup;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

@Mixin(SpawnGroup.class)
public class SpawnGroupMixin {

    /**
     * Guarantees that all spawn groups can spawn in peaceful mode regardless of the value set in the enum.
     * The only spawn group that has the peaceful parameter set to false is {@link SpawnGroup#MONSTER}, so this should be fine.
     */
    @ModifyReturnValue(method = "isPeaceful", at = @At("RETURN"))
    private boolean alwaysIsPeaceful(boolean original) {
        return original || DoormatSettings.peacefulMonsterSpawning.isEnabled();
    }

}
