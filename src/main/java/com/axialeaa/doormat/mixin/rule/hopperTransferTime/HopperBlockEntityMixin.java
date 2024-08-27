package com.axialeaa.doormat.mixin.rule.hopperTransferTime;

import com.axialeaa.doormat.settings.DoormatSettings;
import com.llamalad7.mixinextras.sugar.Local;
import net.minecraft.block.entity.HopperBlockEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Constant;
import org.spongepowered.asm.mixin.injection.ModifyArg;
import org.spongepowered.asm.mixin.injection.ModifyConstant;

@Mixin(HopperBlockEntity.class)
public class HopperBlockEntityMixin {

    @ModifyArg(method = "insertAndExtract", at = @At(value = "INVOKE", target = "Lnet/minecraft/block/entity/HopperBlockEntity;setTransferCooldown(I)V"))
    private static int modifyTransferTime(int original) {
        return DoormatSettings.hopperTransferTime;
    }

    @ModifyArg(method = "transfer(Lnet/minecraft/inventory/Inventory;Lnet/minecraft/inventory/Inventory;Lnet/minecraft/item/ItemStack;ILnet/minecraft/util/math/Direction;)Lnet/minecraft/item/ItemStack;", at = @At(value = "INVOKE", target = "Lnet/minecraft/block/entity/HopperBlockEntity;setTransferCooldown(I)V"))
    private static int modifyTransferDecrement(int i, @Local(ordinal = 1) int j) {
        return DoormatSettings.hopperTransferTime - j;
    }

    @ModifyConstant(method = "isDisabled", constant = @Constant(intValue = 8))
    private int modifyDisabledCheck(int original) {
        return DoormatSettings.hopperTransferTime;
    }

}