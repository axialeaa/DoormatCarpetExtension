package com.axialeaa.doormat.mixin.block_entity;

import com.axialeaa.doormat.DoormatSettings;
import net.minecraft.block.entity.PistonBlockEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.Constant;
import org.spongepowered.asm.mixin.injection.ModifyConstant;

@Mixin(PistonBlockEntity.class)
public class PistonBlockEntityMixin_Timing {

    @ModifyConstant(method = "tick", constant = @Constant(floatValue = 0.5f))
    private static float adjustTiming(float constant) {
        return 1.0f / DoormatSettings.pistonMovementTime; // this number is the amount of blocks moved per tick
    }

}
