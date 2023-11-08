package com.axialeaa.doormat.mixin.rules.pistonMovementTime;

import com.axialeaa.doormat.DoormatSettings;
import net.minecraft.block.entity.PistonBlockEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.Constant;
import org.spongepowered.asm.mixin.injection.ModifyConstant;

@Mixin(PistonBlockEntity.class)
public class PistonBlockEntityMixin {

    /**
     * @return the distance moved per tick in blocks.
     */
    @ModifyConstant(method = "tick", constant = @Constant(floatValue = 0.5F))
    private static float adjustTiming(float constant) {
        return 1.0F / DoormatSettings.pistonMovementTime;
    }

}
