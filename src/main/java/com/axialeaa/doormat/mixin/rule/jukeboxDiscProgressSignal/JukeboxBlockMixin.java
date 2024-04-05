package com.axialeaa.doormat.mixin.rule.jukeboxDiscProgressSignal;

import com.axialeaa.doormat.DoormatSettings;
import com.llamalad7.mixinextras.injector.ModifyReturnValue;
import com.llamalad7.mixinextras.sugar.Local;
import net.minecraft.block.JukeboxBlock;
import net.minecraft.block.entity.JukeboxBlockEntity;
import net.minecraft.item.MusicDiscItem;
import net.minecraft.util.math.MathHelper;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

@Mixin(JukeboxBlock.class)
public class JukeboxBlockMixin {

    /**
     * @return for as long as the rule is enabled, an integer between 1 and 15 based on the fraction of the disc played, otherwise the disc index as normal.
     * @implNote Note from happy Axia: "this is so FREAKING smart i'm so happy eeeheeehee"
     */
    @ModifyReturnValue(method = "getComparatorOutput", at = @At(value = "RETURN", ordinal = 0))
    private int modifyComparatorOutput(int original, @Local JukeboxBlockEntity jukebox, @Local MusicDiscItem disc) {
        long timePlaying = jukebox.tickCount - jukebox.recordStartTick;
        return DoormatSettings.jukeboxDiscProgressSignal ?
            MathHelper.lerpPositive(timePlaying / (float)disc.getSongLengthInTicks(), 0, 15) :
            original;
    }

}
