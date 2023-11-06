package com.axialeaa.doormat.mixin.rules.chiseledBookshelfSignal;

import com.axialeaa.doormat.DoormatSettings;
import com.llamalad7.mixinextras.injector.ModifyReturnValue;
import com.llamalad7.mixinextras.sugar.Local;
import net.minecraft.block.ChiseledBookshelfBlock;
import net.minecraft.block.entity.ChiseledBookshelfBlockEntity;
import net.minecraft.util.math.MathHelper;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

@Mixin(ChiseledBookshelfBlock.class)
public class ChiseledBookshelfBlockMixin {

    @ModifyReturnValue(method = "getComparatorOutput", at = @At(value = "RETURN", ordinal = 1))
    private int modifyComparatorOutput(int original, @Local ChiseledBookshelfBlockEntity chiseledBookshelfBlockEntity) {
        int bookCount = chiseledBookshelfBlockEntity.getOpenSlotCount();
        return switch (DoormatSettings.chiseledBookshelfSignalBasis) {
            case INTERACTION -> original; // if the rule is set to interaction, return vanilla behaviour
            case FULLNESS -> bookCount; // if the rule is set to fullness, return the number of books
            case FULLNESS_LERPED -> MathHelper.lerpPositive(bookCount / (float)chiseledBookshelfBlockEntity.size(), 0, 15);
            // if the rule is set to fullness_lerped, return the number of books interpolated through 1 and 15
        };
    }

}
