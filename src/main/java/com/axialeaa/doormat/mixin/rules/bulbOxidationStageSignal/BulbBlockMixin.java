package com.axialeaa.doormat.mixin.rules.bulbOxidationStageSignal;

import com.axialeaa.doormat.DoormatSettings;
import com.llamalad7.mixinextras.injector.ModifyReturnValue;
import net.minecraft.block.BlockState;
import net.minecraft.block.BulbBlock;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

@Mixin(BulbBlock.class)
public class BulbBlockMixin {

    @ModifyReturnValue(method = "getComparatorOutput", at = @At(value = "RETURN"))
    private int modifyComparatorOutput(int original, BlockState state, World world, BlockPos pos) {
        return DoormatSettings.bulbOxidationStageSignal ? state.getLuminance() : original;
    }

}
