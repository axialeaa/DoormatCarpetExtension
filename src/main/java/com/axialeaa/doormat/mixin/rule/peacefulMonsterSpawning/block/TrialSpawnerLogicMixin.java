package com.axialeaa.doormat.mixin.rule.peacefulMonsterSpawning.block;

import com.axialeaa.doormat.helper.rule.PeacefulMonsterSpawningHelper;
import com.llamalad7.mixinextras.injector.ModifyExpressionValue;
import net.minecraft.block.spawner.TrialSpawnerLogic;
import net.minecraft.world.Difficulty;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

@Mixin(TrialSpawnerLogic.class)
public class TrialSpawnerLogicMixin {

    /**
     * Bypasses the peaceful check for summoning mobs from trial spawners.
     */
    @ModifyExpressionValue(method = "canActivate", at = @At(value = "FIELD", target = "Lnet/minecraft/world/Difficulty;PEACEFUL:Lnet/minecraft/world/Difficulty;"))
    private Difficulty allowPeacefulSpawns(Difficulty original) {
        return PeacefulMonsterSpawningHelper.bypassCheck(original);
    }

}
