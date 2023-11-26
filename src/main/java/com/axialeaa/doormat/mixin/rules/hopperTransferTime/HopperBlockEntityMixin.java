package com.axialeaa.doormat.mixin.rules.hopperTransferTime;

import com.axialeaa.doormat.DoormatSettings;
import net.minecraft.block.entity.HopperBlockEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.Constant;
import org.spongepowered.asm.mixin.injection.ModifyConstant;

@Mixin(HopperBlockEntity.class)
public class HopperBlockEntityMixin {

    @ModifyConstant(method = "insertAndExtract", constant = @Constant(intValue = 8))
    private static int modifyTransferTime(int constant) {
        return DoormatSettings.hopperTransferTime;
    }

}