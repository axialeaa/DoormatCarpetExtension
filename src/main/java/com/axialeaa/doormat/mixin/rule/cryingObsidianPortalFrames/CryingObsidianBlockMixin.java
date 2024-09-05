package com.axialeaa.doormat.mixin.rule.cryingObsidianPortalFrames;

import com.axialeaa.doormat.setting.DoormatSettings;
import com.axialeaa.doormat.util.NetherPortalFrameBlock;
import net.minecraft.block.BlockState;
import net.minecraft.block.CryingObsidianBlock;
import org.spongepowered.asm.mixin.Mixin;

@Mixin(CryingObsidianBlock.class)
public class CryingObsidianBlockMixin implements NetherPortalFrameBlock {

    @Override
    public boolean isValid(BlockState state) {
        return DoormatSettings.cryingObsidianPortalFrames;
    }

}
