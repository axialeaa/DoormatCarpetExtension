package com.axialeaa.doormat.mixin.rule.comparatorsReadThrough;

import com.axialeaa.doormat.DoormatSettings;
import com.axialeaa.doormat.fake.ComparatorBehaviour;
import net.minecraft.block.BlockState;
import net.minecraft.block.DirtPathBlock;
import org.spongepowered.asm.mixin.Mixin;

@Mixin(DirtPathBlock.class)
public class DirtPathBlockMixin implements ComparatorBehaviour {

    @Override
    public boolean doormat$canReadThrough(BlockState state) {
        return DoormatSettings.comparatorsReadThroughPaths;
    }

}
