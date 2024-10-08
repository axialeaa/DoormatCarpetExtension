package com.axialeaa.doormat.mixin.rule.comparatorsReadThrough;

import com.axialeaa.doormat.setting.DoormatSettings;
import com.axialeaa.doormat.util.ComparatorSolidBlock;
import net.minecraft.block.BlockState;
import net.minecraft.block.ChainBlock;
import org.spongepowered.asm.mixin.Mixin;

@Mixin(ChainBlock.class)
public class ChainBlockMixin implements ComparatorSolidBlock {

    @Override
    public boolean isValid(BlockState state) {
        return DoormatSettings.comparatorsReadThroughChains;
    }

}
