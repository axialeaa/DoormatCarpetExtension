package com.axialeaa.doormat.mixin.rule.chiseledBookshelfSignal;

import com.axialeaa.doormat.DoormatSettings;
import com.llamalad7.mixinextras.injector.ModifyReturnValue;
import com.llamalad7.mixinextras.sugar.Local;
import net.minecraft.block.BlockState;
import net.minecraft.block.ChiseledBookshelfBlock;
import net.minecraft.block.entity.ChiseledBookshelfBlockEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

@Mixin(ChiseledBookshelfBlock.class)
public class ChiseledBookshelfBlockMixin {

    @SuppressWarnings("unused")
    @ModifyReturnValue(method = "getComparatorOutput", at = @At(value = "RETURN", ordinal = 1))
    private int test(int original, BlockState state, World world, BlockPos pos, @Local ChiseledBookshelfBlockEntity blockEntity) {
        int size = blockEntity.size();
        int openSlots = blockEntity.getOpenSlotCount();
        return switch (DoormatSettings.chiseledBookshelfSignal) {
            case INTERACTION -> original;
            case FULLNESS -> openSlots;
            case FULLNESS_LERPED -> MathHelper.lerpPositive(openSlots / (float)size, 0, 15);
        };
    }

}
