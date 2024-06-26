package com.axialeaa.doormat.mixin.rule.generatorProduct;

import com.axialeaa.doormat.DoormatSettings;
import com.axialeaa.doormat.helper.BlockIdentifierRuleHelper;
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
        return BlockIdentifierRuleHelper.getBlockFromId(DoormatSettings.stoneGenProduct).orElse(original.call());
    }

}
