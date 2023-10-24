package com.axialeaa.doormat.mixin.rule.pistonMovementTime;

import com.axialeaa.doormat.DoormatSettings;
import net.minecraft.block.entity.PistonBlockEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.Constant;
import org.spongepowered.asm.mixin.injection.ModifyConstant;

@Mixin(PistonBlockEntity.class)
public class PistonBlockEntityMixin {

    @ModifyConstant(method = "tick", constant = @Constant(floatValue = 0.5F))
    private static float adjustTiming(float constant) {
        return 1F / DoormatSettings.pistonMovementTime; // the amount of blocks moved per tick
    }

}
