package com.axialeaa.doormat.mixin.rule.comparatorsReadThrough;

import com.axialeaa.doormat.settings.DoormatSettings;
import com.axialeaa.doormat.fake.ComparatorBehaviour;
import net.minecraft.block.BlockState;
import net.minecraft.block.ChainBlock;
import org.spongepowered.asm.mixin.Mixin;

@Mixin(ChainBlock.class)
public class ChainBlockMixin implements ComparatorBehaviour {

    @Override
    public boolean canReadThrough(BlockState state) {
        return DoormatSettings.comparatorsReadThroughChains;
    }

}
