package com.axialeaa.doormat.mixin.rules.renewableGildedBlackstone;

import net.minecraft.block.AbstractBlock;
import net.minecraft.block.Blocks;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyArg;
import org.spongepowered.asm.mixin.injection.Slice;

@Mixin(Blocks.class)
public class BlocksMixin {

    @ModifyArg(method = "<clinit>", at = @At(value = "INVOKE", target = "net/minecraft/block/MagmaBlock.<init>(Lnet/minecraft/block/AbstractBlock$Settings;)V", ordinal = 0), slice = @Slice(from = @At(value = "CONSTANT", args = "stringValue=magma_block")))
    private static AbstractBlock.Settings magmaRandomTick(AbstractBlock.Settings settings) {
        return settings.ticksRandomly();
        // this is unfortunately necessary as settings assigned to the block when registered cannot be dynamically changed with a rules
        // it doesn't practically change anything though, because the randomTick method in the magma block class checks for the rules right away
    }

}
