package com.axialeaa.doormat.mixin.rules.peacefulMonsterSpawning.world;

import com.axialeaa.doormat.DoormatSettings;
import com.llamalad7.mixinextras.injector.ModifyExpressionValue;
import com.llamalad7.mixinextras.injector.ModifyReturnValue;
import com.llamalad7.mixinextras.sugar.Local;
import net.minecraft.world.Difficulty;
import net.minecraft.world.LocalDifficulty;
import org.objectweb.asm.Opcodes;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@SuppressWarnings("all")
@Mixin(LocalDifficulty.class)
public class LocalDifficultyMixin {

    /**
     * Bypasses the initial Difficulty.PEACEFUL check which otherwise makes the whole method return 0.0 at the head, preventing mobs from spawning altogether.
     */
    @ModifyExpressionValue(method = "setLocalDifficulty", at = @At(value = "FIELD", opcode = Opcodes.GETSTATIC, ordinal = 0))
    private Difficulty test(Difficulty original) {
        return DoormatSettings.peacefulMonsterSpawning.isEnabled() ? null : original;
    }

    /**
     * Injects the same variable reassignment for peaceful mode as this method does for easy, in vanilla.
     */
    @Inject(method = "setLocalDifficulty", at = @At(value = "FIELD", opcode = Opcodes.GETSTATIC, ordinal = 2, shift = At.Shift.BEFORE))
    private void test(Difficulty difficulty, long timeOfDay, long inhabitedTime, float moonSize, CallbackInfoReturnable<Float> cir, @Local(ordinal = 3) float h) {
        if (DoormatSettings.peacefulMonsterSpawning.isEnabled() && difficulty == Difficulty.PEACEFUL)
            h *= 0.5F;
    }

    /**
     * @return the normal return value of this method if the rule is disabled, otherwise the difficulty ID or 1, whichever is largest.
     * This essentially stops it ever reaching 0 and disabling peaceful mode behaviour.
     */
    @ModifyReturnValue(method = "setLocalDifficulty", at = @At(value = "RETURN", ordinal = 1))
    private float test(float original, Difficulty difficulty) {
        return DoormatSettings.peacefulMonsterSpawning.isEnabled() ? Math.max(difficulty.getId(), 1) : original;
    }

}
