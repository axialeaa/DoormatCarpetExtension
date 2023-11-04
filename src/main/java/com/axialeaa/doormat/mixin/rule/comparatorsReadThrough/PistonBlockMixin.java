package com.axialeaa.doormat.mixin.rule.comparatorsReadThrough;

import com.axialeaa.doormat.DoormatSettings;
import com.axialeaa.doormat.fakes.BlockComparatorBehaviourInterface;
import net.minecraft.block.Block;
import net.minecraft.block.PistonBlock;
import org.spongepowered.asm.mixin.Mixin;

@Mixin(PistonBlock.class)
public class PistonBlockMixin implements BlockComparatorBehaviourInterface {

    @Override
    public boolean canReadThrough(Block block) {
        return DoormatSettings.comparatorsReadThroughPistons;
    }

}
