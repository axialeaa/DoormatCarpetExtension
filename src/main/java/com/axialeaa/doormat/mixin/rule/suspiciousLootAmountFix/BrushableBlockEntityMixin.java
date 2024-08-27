package com.axialeaa.doormat.mixin.rule.suspiciousLootAmountFix;

import com.axialeaa.doormat.settings.DoormatSettings;
import net.minecraft.block.entity.BrushableBlockEntity;
import net.minecraft.item.ItemStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyArg;

@Mixin(BrushableBlockEntity.class)
public class BrushableBlockEntityMixin {

    @Shadow private ItemStack item;

    @ModifyArg(method = "spawnItem", at = @At(value = "INVOKE", target = "Lnet/minecraft/item/ItemStack;split(I)Lnet/minecraft/item/ItemStack;"))
    private int spawnEntireStack(int amount) {
        return DoormatSettings.suspiciousLootAmountFix ? this.item.getCount() : amount;
    }

}
