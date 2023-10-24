package com.axialeaa.doormat.mixin.rule.propagulePropagation;

import com.axialeaa.doormat.DoormatSettings;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.block.PropaguleBlock;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(net.minecraft.entity.FallingBlockEntity.class)
public class FallingBlockEntity {

    @Shadow private BlockState block;

    @Inject(method = "tick", at = @At(value = "HEAD"))
    private void modifyMangrovePropagule(CallbackInfo ci) {
        if (DoormatSettings.propagulePropagation && this.block.isOf(Blocks.MANGROVE_PROPAGULE) && this.block.get(PropaguleBlock.HANGING) && this.block.get(PropaguleBlock.AGE) == 4)
            // if the rule is enabled and the falling block entity is a fully grown hanging mangrove propagule, set it to the default mangrove propagule state (standing)
            // this allows it to plant itself on landing
            this.block = Blocks.MANGROVE_PROPAGULE.getDefaultState();
    }

}
