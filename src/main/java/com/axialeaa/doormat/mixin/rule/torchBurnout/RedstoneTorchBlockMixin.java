package com.axialeaa.doormat.mixin.rule.torchBurnout;

import com.axialeaa.doormat.DoormatSettings;
import net.minecraft.block.RedstoneTorchBlock;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.Constant;
import org.spongepowered.asm.mixin.injection.ModifyConstant;

@Mixin(RedstoneTorchBlock.class)
public class RedstoneTorchBlockMixin {

    @ModifyConstant(method = "scheduledTick", constant = @Constant(longValue = 60))
    private long modifyTime(long constant) {
        return DoormatSettings.torchBurnoutTime;
    }

    @ModifyConstant(method = "isBurnedOut", constant = @Constant(intValue = 8))
    private static int modifyMaxFlickerAmount(int constant) {
        return DoormatSettings.torchBurnoutFlickerAmount;
    }

    @ModifyConstant(method = "scheduledTick", constant = @Constant(intValue = 160))
    private int modifyCooldownTime(int constant) {
        return DoormatSettings.torchBurnoutCooldownTime;
    }

}
