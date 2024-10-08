package com.axialeaa.doormat.mixin.rule.generatorProduct;

import com.axialeaa.doormat.setting.DoormatSettings;
import com.axialeaa.doormat.setting.validator.BlockIdentifierValidator;
import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import net.minecraft.block.Block;
import net.minecraft.fluid.LavaFluid;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

@Mixin(LavaFluid.class)
public class LavaFluidMixin {

    @WrapOperation(method = "flow", at = @At(value = "FIELD", target = "Lnet/minecraft/block/Blocks;STONE:Lnet/minecraft/block/Block;"))
    private Block modifyStoneGenProduct(Operation<Block> original) {
        return BlockIdentifierValidator.getRandomBlockFromString(DoormatSettings.stoneGenProduct, original.call());
    }

}
