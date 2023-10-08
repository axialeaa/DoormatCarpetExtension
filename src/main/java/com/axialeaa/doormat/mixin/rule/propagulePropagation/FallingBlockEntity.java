package com.axialeaa.doormat.mixin.rule.propagulePropagation;

import com.axialeaa.doormat.DoormatSettings;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.block.PropaguleBlock;
import org.spongepowered.asm.mixin.Debug;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Debug(export = true)
@Mixin(net.minecraft.entity.FallingBlockEntity.class)
public class FallingBlockEntity {

    @Shadow private BlockState block;

    @Inject(method = "tick", at = @At(value = "HEAD"))
    private void modifyMangrovePropagule(CallbackInfo ci) {
        if (DoormatSettings.propagulePropagation && this.block.isOf(Blocks.MANGROVE_PROPAGULE) && this.block.get(PropaguleBlock.HANGING) && this.block.get(PropaguleBlock.AGE) == 4)
            this.block = Blocks.MANGROVE_PROPAGULE.getDefaultState();
    }

}
