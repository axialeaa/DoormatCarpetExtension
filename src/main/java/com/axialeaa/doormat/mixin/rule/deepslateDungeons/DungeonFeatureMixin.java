package com.axialeaa.doormat.mixin.rule.deepslateDungeons;

import com.axialeaa.doormat.setting.DoormatSettings;
import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import com.llamalad7.mixinextras.sugar.Local;
import net.minecraft.block.Block;
import net.minecraft.block.Blocks;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.gen.feature.DungeonFeature;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

@Mixin(DungeonFeature.class)
public class DungeonFeatureMixin {

    @WrapOperation(method = "generate", at = {
        @At(value = "FIELD", target = "Lnet/minecraft/block/Blocks;COBBLESTONE:Lnet/minecraft/block/Block;"),
        @At(value = "FIELD", target = "Lnet/minecraft/block/Blocks;MOSSY_COBBLESTONE:Lnet/minecraft/block/Block;")
    })
    private Block replaceCobblestone(Operation<Block> original, @Local(ordinal = 0) BlockPos blockPos) {
        return DoormatSettings.deepslateDungeons && blockPos.getY() < 0 ? Blocks.COBBLED_DEEPSLATE : original.call();
    }

}