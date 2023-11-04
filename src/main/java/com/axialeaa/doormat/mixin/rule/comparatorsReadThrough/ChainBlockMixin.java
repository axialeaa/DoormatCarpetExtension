package com.axialeaa.doormat.mixin.rule.comparatorsReadThrough;

import com.axialeaa.doormat.DoormatSettings;
import com.axialeaa.doormat.fakes.BlockComparatorBehaviourInterface;
import net.minecraft.block.Block;
import net.minecraft.block.ChainBlock;
import org.spongepowered.asm.mixin.Mixin;

@Mixin(ChainBlock.class)
public class ChainBlockMixin implements BlockComparatorBehaviourInterface {

    @Override
    public boolean canReadThrough(Block block) {
        return DoormatSettings.comparatorsReadThroughChains;
    }

}