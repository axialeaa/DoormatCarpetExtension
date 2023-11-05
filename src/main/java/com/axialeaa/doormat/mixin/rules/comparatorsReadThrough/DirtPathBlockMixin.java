package com.axialeaa.doormat.mixin.rules.comparatorsReadThrough;

import com.axialeaa.doormat.DoormatSettings;
import com.axialeaa.doormat.interfaces.BlockComparatorBehaviourInterface;
import net.minecraft.block.Block;
import net.minecraft.block.DirtPathBlock;
import org.spongepowered.asm.mixin.Mixin;

@Mixin(DirtPathBlock.class)
public class DirtPathBlockMixin implements BlockComparatorBehaviourInterface {

    @Override
    public boolean canReadThrough(Block block) {
        return DoormatSettings.comparatorsReadThroughPaths;
    }

}
