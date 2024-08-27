package com.axialeaa.doormat.mixin.rule.woolDesertPyramids;

import com.axialeaa.doormat.settings.DoormatSettings;
import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import net.minecraft.block.Block;
import net.minecraft.block.Blocks;
import net.minecraft.structure.DesertTempleGenerator;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

@Mixin(DesertTempleGenerator.class)
public class DesertTempleGeneratorMixin {

    @WrapOperation(method = "generate", at = @At(value = "FIELD", target = "Lnet/minecraft/block/Blocks;ORANGE_TERRACOTTA:Lnet/minecraft/block/Block;"))
    private Block replaceOrange(Operation<Block> original) {
        return DoormatSettings.woolDesertPyramids ? Blocks.ORANGE_WOOL : original.call();
    }

    @WrapOperation(method = "generate", at = @At(value = "FIELD", target = "Lnet/minecraft/block/Blocks;BLUE_TERRACOTTA:Lnet/minecraft/block/Block;"))
    private Block replaceBlue(Operation<Block> original) {
        return DoormatSettings.woolDesertPyramids ? Blocks.BLUE_WOOL : original.call();
    }

}
