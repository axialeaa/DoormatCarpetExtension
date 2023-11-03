package com.axialeaa.doormat.mixin.rule.comparatorsReadThrough;

import com.axialeaa.doormat.DoormatSettings;
import com.axialeaa.doormat.helpers.ConditionalRedstoneBehavior;
import net.minecraft.block.Block;
import net.minecraft.block.ChainBlock;
import org.spongepowered.asm.mixin.Mixin;

@Mixin(ChainBlock.class)
public abstract class ChainBlockMixin implements ConditionalRedstoneBehavior {

    @Override
    public boolean canReadThroughBlock(Block block) {
        return DoormatSettings.comparatorsReadThroughChains;
    }

}
