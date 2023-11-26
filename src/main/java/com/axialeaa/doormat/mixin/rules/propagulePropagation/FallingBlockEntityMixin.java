package com.axialeaa.doormat.mixin.rules.propagulePropagation;

import com.axialeaa.doormat.DoormatSettings;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.block.PropaguleBlock;
import net.minecraft.entity.FallingBlockEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(FallingBlockEntity.class)
public class FallingBlockEntityMixin {

    @Shadow private BlockState block;

    /**
     * As long as the rule is enabled, sets falling, hanging mangrove propagule entities to standing variants when they hit the ground, allowing them to plant themselves on landing.
     */
    @Inject(method = "tick", at = @At(value = "HEAD"))
    private void modifyMangrovePropagule(CallbackInfo ci) {
        if (DoormatSettings.propagulePropagation && this.block.isOf(Blocks.MANGROVE_PROPAGULE) && this.block.get(PropaguleBlock.HANGING) && this.block.get(PropaguleBlock.AGE) == 4)
            this.block = Blocks.MANGROVE_PROPAGULE.getDefaultState();
    }

}