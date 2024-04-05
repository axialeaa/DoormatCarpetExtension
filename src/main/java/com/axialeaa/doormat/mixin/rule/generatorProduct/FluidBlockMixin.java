package com.axialeaa.doormat.mixin.rule.generatorProduct;

import com.axialeaa.doormat.DoormatSettings;
import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import net.minecraft.block.Block;
import net.minecraft.block.FluidBlock;
import net.minecraft.registry.Registries;
import net.minecraft.util.Identifier;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

@Mixin(FluidBlock.class)
public class FluidBlockMixin {

    @WrapOperation(method = "receiveNeighborFluids", at = @At(value = "FIELD", target = "Lnet/minecraft/block/Blocks;OBSIDIAN:Lnet/minecraft/block/Block;"))
    private Block modifyObsidianGenProduct(Operation<Block> original) {
        Identifier id = Identifier.tryParse(DoormatSettings.obsidianGenProduct);
        return id == null ? original.call() : Registries.BLOCK.get(id);
    }

    @WrapOperation(method = "receiveNeighborFluids", at = @At(value = "FIELD", target = "Lnet/minecraft/block/Blocks;COBBLESTONE:Lnet/minecraft/block/Block;"))
    private Block modifyCobbleGenProduct(Operation<Block> original) {
        Identifier id = Identifier.tryParse(DoormatSettings.cobblestoneGenProduct);
        return id == null ? original.call() : Registries.BLOCK.get(id);
    }

    @WrapOperation(method = "receiveNeighborFluids", at = @At(value = "FIELD", target = "Lnet/minecraft/block/Blocks;BASALT:Lnet/minecraft/block/Block;"))
    private Block modifyBasaltGenProduct(Operation<Block> original) {
        Identifier id = Identifier.tryParse(DoormatSettings.basaltGenProduct);
        return id == null ? original.call() : Registries.BLOCK.get(id);
    }

}
