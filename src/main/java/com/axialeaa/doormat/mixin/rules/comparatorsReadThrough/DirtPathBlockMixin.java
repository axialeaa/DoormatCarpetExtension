package com.axialeaa.doormat.mixin.rules.comparatorsReadThrough;

import com.axialeaa.doormat.DoormatSettings;
import com.axialeaa.doormat.interfaces.ComparatorBehaviourInterface;
import net.minecraft.block.Block;
import net.minecraft.block.DirtPathBlock;
import org.spongepowered.asm.mixin.Mixin;

@Mixin(DirtPathBlock.class)
public class DirtPathBlockMixin implements ComparatorBehaviourInterface {

    @Override
    public boolean doormat$canReadThrough(Block block) {
        return DoormatSettings.comparatorsReadThroughPaths;
    }

}
