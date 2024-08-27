package com.axialeaa.doormat.mixin.rule.chiseledBookshelfFullnessSignal;

import com.axialeaa.doormat.settings.DoormatSettings;
import com.llamalad7.mixinextras.injector.ModifyReturnValue;
import com.llamalad7.mixinextras.sugar.Local;
import net.minecraft.block.ChiseledBookshelfBlock;
import net.minecraft.block.entity.ChiseledBookshelfBlockEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

@Mixin(ChiseledBookshelfBlock.class)
public class ChiseledBookshelfBlockMixin {

    /**
     * @param original the default behaviour
     * @param blockEntity the chiselled bookshelf block entity in question
     * @return the number of books in the bookshelf if the rule is set to fullness or the same number interpolated between 1 and 15 if set to fullness_lerped, otherwise vanilla behaviour.
     */
    @ModifyReturnValue(method = "getComparatorOutput", at = @At(value = "RETURN", ordinal = 1))
    private int modifyComparatorOutput(int original, @Local ChiseledBookshelfBlockEntity blockEntity) {
        return DoormatSettings.chiseledBookshelfFullnessSignal.getOutput(blockEntity);
    }

}
