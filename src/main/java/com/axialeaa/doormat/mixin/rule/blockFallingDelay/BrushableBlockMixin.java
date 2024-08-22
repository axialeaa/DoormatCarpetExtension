package com.axialeaa.doormat.mixin.rule.blockFallingDelay;

import com.axialeaa.doormat.DoormatSettings;
import net.minecraft.block.BrushableBlock;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyArg;

@Mixin(BrushableBlock.class)
public class BrushableBlockMixin {
    
    @ModifyArg(method = { "onBlockAdded", "getStateForNeighborUpdate" }, at = @At(value = "INVOKE", target = "Lnet/minecraft/world/World;scheduleBlockTick(Lnet/minecraft/util/math/BlockPos;Lnet/minecraft/block/Block;I)V"), index = 2)
    private int modifyFallDelay(int original) {
        return DoormatSettings.blockFallingDelay;
    }
    
}
