package com.axialeaa.doormat.mixin.rules.peacefulMonsterSpawning.world;

import com.axialeaa.doormat.DoormatSettings;
import com.axialeaa.doormat.helpers.PeacefulSpawningHelper;
import com.llamalad7.mixinextras.injector.ModifyExpressionValue;
import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import com.llamalad7.mixinextras.sugar.Local;
import net.minecraft.world.Difficulty;
import net.minecraft.world.LocalDifficulty;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(LocalDifficulty.class)
public class LocalDifficultyMixin {

    /**
     * Bypasses the initial Difficulty.PEACEFUL check which otherwise makes the whole method return 0.0 at the head, preventing mobs from spawning altogether.
     */
    @ModifyExpressionValue(method = "setLocalDifficulty", at = @At(value = "FIELD", target = "Lnet/minecraft/world/Difficulty;PEACEFUL:Lnet/minecraft/world/Difficulty;"))
    private Difficulty bypassPeacefulCheck(Difficulty original) {
        return PeacefulSpawningHelper.bypassCheck(original);
    }

    /**
     * Injects the same variable reassignment for peaceful mode as this method does for easy, in vanilla.
     */
    @Inject(method = "setLocalDifficulty", at = @At(value = "FIELD", target = "Lnet/minecraft/world/Difficulty;EASY:Lnet/minecraft/world/Difficulty;", shift = At.Shift.BEFORE))
    private void reassignVarForPeaceful(Difficulty difficulty, long timeOfDay, long inhabitedTime, float moonSize, CallbackInfoReturnable<Float> cir, @Local(ordinal = 3) float h) {
        if (DoormatSettings.peacefulMonsterSpawning.enabled() && difficulty == Difficulty.PEACEFUL)
            h *= 0.5F;
    }

    /**
     * @return the normal return value of this method if the rule is disabled, otherwise the difficulty ID or 1, whichever is largest.
     * This essentially stops it ever reaching 0 and disabling peaceful mode behaviour.
     */
    @WrapOperation(method = "setLocalDifficulty", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/Difficulty;getId()I"))
    private int capAbovePeacefulId(Difficulty difficulty, Operation<Integer> original) {
        return Math.max(original.call(difficulty), DoormatSettings.peacefulMonsterSpawning.enabled() ? 1 : 0);
    }

}
