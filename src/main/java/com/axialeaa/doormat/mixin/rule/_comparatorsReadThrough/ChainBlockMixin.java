package com.axialeaa.doormat.mixin.rule._comparatorsReadThrough;

import com.axialeaa.doormat.DoormatSettings;
import com.axialeaa.doormat.fakes.BlockComparatorBehaviorInterface;
import net.minecraft.block.Block;
import net.minecraft.block.ChainBlock;
import org.spongepowered.asm.mixin.Mixin;

@Mixin(ChainBlock.class)
public class ChainBlockMixin implements BlockComparatorBehaviorInterface {

    @Override
    public boolean canReadThrough(Block block) {
        return DoormatSettings.comparatorsReadThroughChains;
    }

}
