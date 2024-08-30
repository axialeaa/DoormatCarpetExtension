package com.axialeaa.doormat.mixin.rule.cryingObsidianPortalFrames;

import com.axialeaa.doormat.setting.DoormatSettings;
import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import net.minecraft.block.AbstractFireBlock;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

@Mixin(AbstractFireBlock.class)
public class AbstractFireBlockMixin {

    @WrapOperation(method = "shouldLightPortalAt", at = @At(value = "INVOKE", target = "Lnet/minecraft/block/BlockState;isOf(Lnet/minecraft/block/Block;)Z"))
    private static boolean acceptCryingObsidian(BlockState instance, Block block, Operation<Boolean> original) {
        return original.call(instance, block) || instance.isOf(Blocks.CRYING_OBSIDIAN) && DoormatSettings.cryingObsidianPortalFrames;
    }

}
