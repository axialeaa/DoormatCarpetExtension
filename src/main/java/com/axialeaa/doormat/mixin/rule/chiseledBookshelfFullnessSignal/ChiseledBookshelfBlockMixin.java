package com.axialeaa.doormat.mixin.rule.chiseledBookshelfFullnessSignal;

import com.axialeaa.doormat.setting.DoormatSettings;
import com.llamalad7.mixinextras.injector.ModifyReturnValue;
import com.llamalad7.mixinextras.sugar.Local;
import net.minecraft.block.ChiseledBookshelfBlock;
import net.minecraft.block.entity.ChiseledBookshelfBlockEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

@Mixin(ChiseledBookshelfBlock.class)
public class ChiseledBookshelfBlockMixin {

    @ModifyReturnValue(method = "getComparatorOutput", at = @At(value = "RETURN", ordinal = 1))
    private int modifyComparatorOutput(int original, @Local ChiseledBookshelfBlockEntity blockEntity) {
        return DoormatSettings.chiseledBookshelfFullnessSignal.getOutput(blockEntity);
    }

}
