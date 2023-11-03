package com.axialeaa.doormat.mixin.rule.comparatorsReadThrough;

import com.axialeaa.doormat.DoormatSettings;
import com.axialeaa.doormat.helpers.ConditionalRedstoneBehavior;
import net.minecraft.block.Block;
import net.minecraft.block.DirtPathBlock;
import org.spongepowered.asm.mixin.Mixin;

@Mixin(DirtPathBlock.class)
public abstract class DirtPathBlockMixin implements ConditionalRedstoneBehavior {

    @Override
    public boolean canReadThroughBlock(Block block) {
        return DoormatSettings.comparatorsReadThroughPaths;
    }

}
