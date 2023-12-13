package com.axialeaa.doormat.mixin.rules.comparatorsReadThrough;

import com.axialeaa.doormat.DoormatSettings;
import com.axialeaa.doormat.interfaces.ComparatorBehaviourInterface;
import net.minecraft.block.Block;
import net.minecraft.block.PistonBlock;
import org.spongepowered.asm.mixin.Mixin;

@Mixin(PistonBlock.class)
public class PistonBlockMixin implements ComparatorBehaviourInterface {

    @Override
    public boolean doormat$canReadThrough(Block block) {
        return DoormatSettings.comparatorsReadThroughPistons;
    }

}
