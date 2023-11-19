package com.axialeaa.doormat.mixin.rules.comparatorsReadThrough;

import com.axialeaa.doormat.DoormatSettings;
import com.axialeaa.doormat.interfaces.BlockComparatorBehaviourInterface;
import net.minecraft.block.Block;
import net.minecraft.block.PistonBlock;
import org.spongepowered.asm.mixin.Mixin;

@Mixin(PistonBlock.class)
public class PistonBlockMixin implements BlockComparatorBehaviourInterface {

    @Override
    public boolean doormat$canReadThrough(Block block) {
        return DoormatSettings.comparatorsReadThroughPistons;
    }

}
