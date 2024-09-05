package com.axialeaa.doormat.mixin.rule.solidHoney;

import com.axialeaa.doormat.setting.DoormatSettings;
import com.llamalad7.mixinextras.injector.ModifyReturnValue;
import net.minecraft.block.AbstractBlock;
import net.minecraft.block.Block;
import net.minecraft.block.HoneyBlock;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;

@Mixin(AbstractBlock.AbstractBlockState.class)
public abstract class AbstractBlockStateMixin {

    @Shadow public abstract Block getBlock();

    @ModifyReturnValue(method = "isSolidBlock", at = @At("RETURN"))
    private boolean isSolidOrHoney(boolean original) {
        return original || DoormatSettings.solidHoney && this.getBlock() instanceof HoneyBlock;
    }

}
