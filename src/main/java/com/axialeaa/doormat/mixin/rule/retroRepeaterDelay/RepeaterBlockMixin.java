package com.axialeaa.doormat.mixin.rule.retroRepeaterDelay;

import com.axialeaa.doormat.DoormatSettings;
import com.llamalad7.mixinextras.injector.ModifyReturnValue;
import com.llamalad7.mixinextras.sugar.Local;
import net.minecraft.block.BlockState;
import net.minecraft.block.RepeaterBlock;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

@Mixin(RepeaterBlock.class)
public class RepeaterBlockMixin {

    @ModifyReturnValue(method = "getUpdateDelayInternal", at = @At("RETURN"))
    private int modifyUpdateDelay(int original, @Local(argsOnly = true) BlockState state) {
        if (!DoormatSettings.retroRepeaterDelay)
            return original;

        return switch (state.get(RepeaterBlock.DELAY)) {
            case 1 -> 2;
            case 2 -> 4;
            case 3 -> 10;
            case 4 -> 14;
            default -> throw new IllegalStateException("Unexpected value: " + state.get(RepeaterBlock.DELAY));
        };
    }

}
