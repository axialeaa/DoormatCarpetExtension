package com.axialeaa.doormat.mixin.rule.generatorProduct;

import com.axialeaa.doormat.DoormatSettings;
import com.axialeaa.doormat.helper.BlockIdentifierRuleHelper;
import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import net.minecraft.block.Block;
import net.minecraft.block.FluidBlock;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

@Mixin(FluidBlock.class)
public class FluidBlockMixin {

    @WrapOperation(method = "receiveNeighborFluids", at = @At(value = "FIELD", target = "Lnet/minecraft/block/Blocks;OBSIDIAN:Lnet/minecraft/block/Block;"))
    private Block modifyObsidianGenProduct(Operation<Block> original) {
        return BlockIdentifierRuleHelper.getBlockFromId(DoormatSettings.obsidianGenProduct).orElse(original.call());
    }

    @WrapOperation(method = "receiveNeighborFluids", at = @At(value = "FIELD", target = "Lnet/minecraft/block/Blocks;COBBLESTONE:Lnet/minecraft/block/Block;"))
    private Block modifyCobbleGenProduct(Operation<Block> original) {
        return BlockIdentifierRuleHelper.getBlockFromId(DoormatSettings.cobblestoneGenProduct).orElse(original.call());
    }

    @WrapOperation(method = "receiveNeighborFluids", at = @At(value = "FIELD", target = "Lnet/minecraft/block/Blocks;BASALT:Lnet/minecraft/block/Block;"))
    private Block modifyBasaltGenProduct(Operation<Block> original) {
        return BlockIdentifierRuleHelper.getBlockFromId(DoormatSettings.basaltGenProduct).orElse(original.call());
    }

}
