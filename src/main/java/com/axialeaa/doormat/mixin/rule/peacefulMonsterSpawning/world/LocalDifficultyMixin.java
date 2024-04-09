package com.axialeaa.doormat.mixin.rule.peacefulMonsterSpawning.world;

import com.axialeaa.doormat.DoormatSettings;
import com.axialeaa.doormat.helper.PeacefulMonsterSpawningHelper;
import com.llamalad7.mixinextras.injector.ModifyExpressionValue;
import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import com.llamalad7.mixinextras.sugar.Local;
import net.minecraft.world.Difficulty;
import net.minecraft.world.LocalDifficulty;
import org.spongepowered.asm.mixin.Debug;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyVariable;

@Debug(export = true)
@Mixin(LocalDifficulty.class)
public class LocalDifficultyMixin {

    /**
     * Bypasses the initial Difficulty.PEACEFUL check which otherwise makes the whole method return 0.0 at the head, preventing mobs from spawning altogether.
     */
    @ModifyExpressionValue(method = "setLocalDifficulty", at = @At(value = "FIELD", target = "Lnet/minecraft/world/Difficulty;PEACEFUL:Lnet/minecraft/world/Difficulty;"))
    private Difficulty bypassPeacefulCheck(Difficulty original) {
        return PeacefulMonsterSpawningHelper.bypassCheck(original);
    }

    /**
     * Injects the same variable reassignment for peaceful mode as this method does for easy, in vanilla.
     */
    @ModifyVariable(method = "setLocalDifficulty", at = @At(value = "FIELD", target = "Lnet/minecraft/world/Difficulty;EASY:Lnet/minecraft/world/Difficulty;", shift = At.Shift.BEFORE), name = "h")
    private float reassignVarForPeaceful(float value, @Local(argsOnly = true) Difficulty difficulty) {
        return DoormatSettings.peacefulMonsterSpawning.enabled() && difficulty == Difficulty.PEACEFUL ? value * 0.5F : value;
    }

    /**
     * @return the normal return value of this method if the rule is disabled, otherwise the difficulty ID or 1, whichever is largest.
     * This essentially stops it ever reaching 0 and disabling peaceful mode behaviour.
     */
    @WrapOperation(method = "setLocalDifficulty", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/Difficulty;getId()I"))
    private int capAbovePeacefulId(Difficulty instance, Operation<Integer> original) {
        return Math.max(original.call(instance), DoormatSettings.peacefulMonsterSpawning.enabled() ? 1 : 0);
    }

}
