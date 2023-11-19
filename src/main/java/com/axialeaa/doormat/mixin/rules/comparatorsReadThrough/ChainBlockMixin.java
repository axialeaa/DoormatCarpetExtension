package com.axialeaa.doormat.mixin.rules.comparatorsReadThrough;

import com.axialeaa.doormat.DoormatSettings;
import com.axialeaa.doormat.interfaces.BlockComparatorBehaviourInterface;
import net.minecraft.block.Block;
import net.minecraft.block.ChainBlock;
import org.spongepowered.asm.mixin.Mixin;

@Mixin(ChainBlock.class)
public class ChainBlockMixin implements BlockComparatorBehaviourInterface {

    @Override
    public boolean doormat$canReadThrough(Block block) {
        return DoormatSettings.comparatorsReadThroughChains;
    }

}
