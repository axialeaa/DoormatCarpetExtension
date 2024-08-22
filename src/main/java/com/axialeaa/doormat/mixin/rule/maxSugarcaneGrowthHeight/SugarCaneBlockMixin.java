package com.axialeaa.doormat.mixin.rule.maxSugarcaneGrowthHeight;

import com.axialeaa.doormat.DoormatSettings;
import net.minecraft.block.SugarCaneBlock;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.Constant;
import org.spongepowered.asm.mixin.injection.ModifyConstant;

@Mixin(SugarCaneBlock.class)
public class SugarCaneBlockMixin {

    @ModifyConstant(method = "randomTick", constant = @Constant(intValue = 3))
    private int modifyGrowthHeight(int original) {
        return DoormatSettings.maxSugarcaneGrowthHeight;
    }

}
