package com.axialeaa.doormat.mixin.rule.comparatorsReadThrough;

import com.axialeaa.doormat.DoormatSettings;
import com.axialeaa.doormat.helpers.ConditionalRedstoneBehavior;
import net.minecraft.block.Block;
import net.minecraft.block.PistonBlock;
import org.spongepowered.asm.mixin.Mixin;

@Mixin(PistonBlock.class)
public abstract class PistonBlockMixin implements ConditionalRedstoneBehavior {

    @Override
    public boolean canReadThroughBlock(Block block) {
        return DoormatSettings.comparatorsReadThroughPistons;
    }

}
