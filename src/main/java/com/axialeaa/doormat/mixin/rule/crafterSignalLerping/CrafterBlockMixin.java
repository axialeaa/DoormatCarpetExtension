package com.axialeaa.doormat.mixin.rule.crafterSignalLerping;

import com.axialeaa.doormat.setting.DoormatSettings;
import com.llamalad7.mixinextras.injector.ModifyReturnValue;
import net.minecraft.block.BlockState;
import net.minecraft.block.CrafterBlock;
import net.minecraft.block.entity.CrafterBlockEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

@Mixin(CrafterBlock.class)
public class CrafterBlockMixin {

    @ModifyReturnValue(method = "getComparatorOutput", at = @At("RETURN"))
    private int lerpComparatorOutput(int original, BlockState state, World world, BlockPos pos) {
        if (!DoormatSettings.crafterSignalLerping || !(world.getBlockEntity(pos) instanceof CrafterBlockEntity crafterBlockEntity))
            return original;

        return MathHelper.lerpPositive(original / (float) crafterBlockEntity.size(), 0, 15);
    }

}
