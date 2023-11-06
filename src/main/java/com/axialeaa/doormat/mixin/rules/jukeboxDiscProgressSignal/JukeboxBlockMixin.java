package com.axialeaa.doormat.mixin.rules.jukeboxDiscProgressSignal;

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

    // this is so FREAKING smart i'm so happy eeeheeehee

    @ModifyReturnValue(method = "getComparatorOutput", at = @At("RETURN"))
    private int modifyComparatorOutput(int original, @Local JukeboxBlockEntity jukebox, @Local MusicDiscItem disc) {
        long timePlaying = jukebox.tickCount - jukebox.recordStartTick; // find the time the jukebox has been playing this disc
        return DoormatSettings.jukeboxDiscProgressSignal ?
            // if the rule is enabled...
            MathHelper.lerpPositive(timePlaying / (float)disc.getSongLengthInTicks(), 0, 15) :
            // interpolate through 1 and 15 based on the fraction of the disc played
            original; // otherwise return the value of the music disc index, as normal
    }

}
