package com.axialeaa.doormat.mixin.rule.comparatorsReadThrough;

import com.axialeaa.doormat.setting.DoormatSettings;
import com.axialeaa.doormat.util.ComparatorSolidBlock;
import net.minecraft.block.BlockState;
import net.minecraft.block.PistonBlock;
import org.spongepowered.asm.mixin.Mixin;

@Mixin(PistonBlock.class)
public class PistonBlockMixin implements ComparatorSolidBlock {

    @Override
    public boolean isValid(BlockState state) {
        return DoormatSettings.comparatorsReadThroughPistons;
    }

}
