package com.axialeaa.doormat.mixin.rule.peacefulMonsterSpawning.world;

import com.axialeaa.doormat.setting.DoormatSettings;
import com.llamalad7.mixinextras.injector.ModifyExpressionValue;
import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import com.llamalad7.mixinextras.sugar.Local;
import net.minecraft.world.Difficulty;
import net.minecraft.world.LocalDifficulty;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

@Mixin(LocalDifficulty.class)
public class LocalDifficultyMixin {

    /**
     * Bypasses the initial Difficulty.PEACEFUL check which otherwise makes the whole method return 0.0 at the head, preventing mobs from spawning altogether.
     */
    @ModifyExpressionValue(method = "setLocalDifficulty", at = @At(value = "FIELD", target = "Lnet/minecraft/world/Difficulty;PEACEFUL:Lnet/minecraft/world/Difficulty;"))
    private Difficulty bypassPeacefulCheck(Difficulty original) {
        return DoormatSettings.peacefulMonsterSpawning.isEnabled() ? Difficulty.EASY : original;
    }

    /**
     * Reassigns the local variable "h" for peaceful mode as well as easy. This is how it looks in vanilla:
     * <pre>
     * {@code
     * if (difficulty == Difficulty.EASY) {
     *     h *= 0.5F;
     * }
     * }
     * </pre>
     * Changing {@code Difficulty.EASY} to {@code difficulty} results in the condition always returning true:
     * <pre>
     * {@code
     * if (difficulty == difficulty) {
     *     h *= 0.5F;
     * }
     * }
     * </pre>
     * The new condition is wrapped around the above modification, like this:
     * <pre>
     * {@code
     * if (difficulty == DoormatSettings.peacefulMonsterSpawning.isEnabled() && (difficulty == Difficulty.PEACEFUL || difficulty == Difficulty.EASY) ? difficulty : Difficulty.EASY) {
     *     h *= 0.5F;
     * }
     * }
     * </pre>
     */
    @ModifyExpressionValue(method = "setLocalDifficulty", at = @At(value = "FIELD", target = "Lnet/minecraft/world/Difficulty;EASY:Lnet/minecraft/world/Difficulty;"))
    private Difficulty modifyReassignment(Difficulty original, @Local(argsOnly = true) Difficulty difficulty) {
        return DoormatSettings.peacefulMonsterSpawning.isEnabled() && (difficulty == Difficulty.PEACEFUL || difficulty == Difficulty.EASY) ?
            difficulty : original;
    }

    /**
     * @return the normal return value of this method if the rule is disabled, otherwise the difficulty ID or 1, whichever is largest.
     * This essentially stops it ever reaching 0 and disabling peaceful mode behaviour.
     */
    @WrapOperation(method = "setLocalDifficulty", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/Difficulty;getId()I"))
    private int capAbovePeacefulId(Difficulty instance, Operation<Integer> original) {
        return Math.max(original.call(instance), DoormatSettings.peacefulMonsterSpawning.isEnabled() ? 1 : 0);
    }

}
