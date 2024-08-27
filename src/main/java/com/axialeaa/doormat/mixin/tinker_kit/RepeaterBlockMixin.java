package com.axialeaa.doormat.mixin.tinker_kit;

import com.axialeaa.doormat.settings.DoormatSettings;
import com.axialeaa.doormat.tinker_kit.TinkerKit;
import com.llamalad7.mixinextras.injector.ModifyReturnValue;
import com.llamalad7.mixinextras.sugar.Local;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.RepeaterBlock;
import net.minecraft.state.property.IntProperty;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;

@Mixin(value = RepeaterBlock.class, priority = 1500)
public class RepeaterBlockMixin {

    @Shadow @Final public static IntProperty DELAY;

    @ModifyReturnValue(method = "getUpdateDelayInternal", at = @At("RETURN"))
    private int modifyDelay(int original, @Local(argsOnly = true) BlockState state) {
        if (DoormatSettings.retroRepeaterDelay)
            return original;

        Block block = state.getBlock();

        return state.get(DELAY) * TinkerKit.getDelay(block, original);
    }

}
