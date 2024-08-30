package com.axialeaa.doormat.mixin.rule.movableEnderChests;

import com.axialeaa.doormat.setting.DoormatSettings;
import com.bawnorton.mixinsquared.TargetHandler;
import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import net.minecraft.block.Block;
import net.minecraft.block.PistonBlock;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

@Mixin(value = PistonBlock.class, priority = 1500)
public class PistonBlockMixin {

    @SuppressWarnings("UnresolvedMixinReference")
    @TargetHandler(mixin = "carpet.mixins.PistonBaseBlock_movableBEMixin", name = "isPushableBlockEntity")
    @WrapOperation(method = "@MixinSquared:Handler", at = @At(value = "FIELD", target = "Lnet/minecraft/block/Blocks;ENDER_CHEST:Lnet/minecraft/block/Block;"))
    private static Block bypassEnderChestCheck(Operation<Block> original) {
        return DoormatSettings.movableEnderChests ? null : original.call();
    }

}
