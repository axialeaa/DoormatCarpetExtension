package com.axialeaa.doormat.mixin.rule.disablePiglinBlockGuarding;

import com.axialeaa.doormat.setting.DoormatSettings;
import com.llamalad7.mixinextras.injector.wrapmethod.WrapMethod;
import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import net.minecraft.entity.mob.PiglinBrain;
import net.minecraft.entity.player.PlayerEntity;
import org.spongepowered.asm.mixin.Mixin;

@Mixin(PiglinBrain.class)
public class PiglinBrainMixin {

    @WrapMethod(method = "onGuardedBlockInteracted")
    private static void cancelAttack(PlayerEntity player, boolean blockOpen, Operation<Void> original) {
        if (!DoormatSettings.disablePiglinBlockGuarding.shouldNegateAnger(player, blockOpen))
            original.call(player, blockOpen);
    }

}
