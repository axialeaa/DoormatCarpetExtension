package com.axialeaa.doormat.mixin.rule.comparatorsReadThrough;

import com.axialeaa.doormat.DoormatSettings;
import com.axialeaa.doormat.fake.ComparatorBehaviour;
import net.minecraft.block.BlockState;
import net.minecraft.block.PistonBlock;
import org.spongepowered.asm.mixin.Mixin;

@Mixin(PistonBlock.class)
public class PistonBlockMixin implements ComparatorBehaviour {

    @Override
    public boolean canReadThrough(BlockState state) {
        return DoormatSettings.comparatorsReadThroughPistons;
    }

}
