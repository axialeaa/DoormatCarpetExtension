package com.axialeaa.doormat.mixin.rule.torchBurnout;

import com.axialeaa.doormat.DoormatSettings;
import net.minecraft.block.RedstoneTorchBlock;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.Constant;
import org.spongepowered.asm.mixin.injection.ModifyConstant;

@Mixin(RedstoneTorchBlock.class)
public class RedstoneTorchBlockMixin {

    /**
     * @return the timespan to measure the flicker amount against.
     */
    @ModifyConstant(method = "scheduledTick", constant = @Constant(longValue = 60))
    private long modifyTime(long constant) {
        return DoormatSettings.torchBurnoutTime;
    }

    /**
     * @return the maximum number of flickers a torch can have in the set timespan before burning out.
     */
    @ModifyConstant(method = "isBurnedOut", constant = @Constant(intValue = 8))
    private static int modifyMaxFlickerAmount(int constant) {
        return DoormatSettings.torchBurnoutFlickerAmount;
    }

}
