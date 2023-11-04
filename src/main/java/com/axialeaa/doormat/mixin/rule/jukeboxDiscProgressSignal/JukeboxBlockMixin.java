package com.axialeaa.doormat.mixin.rule.jukeboxDiscProgressSignal;

import com.axialeaa.doormat.DoormatSettings;
import com.llamalad7.mixinextras.injector.ModifyReturnValue;
import com.llamalad7.mixinextras.sugar.Local;
import net.minecraft.block.BlockState;
import net.minecraft.block.JukeboxBlock;
import net.minecraft.block.entity.JukeboxBlockEntity;
import net.minecraft.item.MusicDiscItem;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

@Mixin(JukeboxBlock.class)
public class JukeboxBlockMixin {

    @SuppressWarnings("unused")
    @ModifyReturnValue(method = "getComparatorOutput", at = @At("RETURN"))
    private int test(int original, BlockState state, World world, BlockPos pos, @Local JukeboxBlockEntity blockEntity, @Local MusicDiscItem musicDisc) {
        long timePlaying = blockEntity.tickCount - blockEntity.recordStartTick;
        return DoormatSettings.jukeboxDiscProgressSignal ?
            MathHelper.lerpPositive(timePlaying / (float)musicDisc.getSongLengthInTicks(), 0, 15) :
            original; // this is so FREAKING smart i'm so happy eeeheeehee
    }

}
