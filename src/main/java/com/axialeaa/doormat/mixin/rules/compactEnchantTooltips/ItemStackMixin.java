package com.axialeaa.doormat.mixin.rules.compactEnchantTooltips;

import com.axialeaa.doormat.DoormatSettings;
import com.axialeaa.doormat.helpers.EnchantmentCarousel;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.nbt.NbtList;
import net.minecraft.registry.Registries;
import net.minecraft.text.Text;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.List;

@Environment(EnvType.CLIENT)
@Mixin(ItemStack.class)
public class ItemStackMixin {

    /**
     * Replaces the enchantment list with a cycling "carousel" if the rule is enabled.
     */
    @Inject(method = "appendEnchantments", at = @At("HEAD"), cancellable = true)
    private static void createCarousel(List<Text> tooltip, NbtList enchantments, CallbackInfo ci) {
        if (DoormatSettings.compactEnchantTooltips) {
            ci.cancel();
            EnchantmentCarousel.listSize = enchantments.size(); // set the listSize variable to the length of the enchantment list
            NbtCompound nbtCompound = enchantments.getCompound(EnchantmentCarousel.listIndex); // get the data of the enchantment at the index set via the helper class
            Registries.ENCHANTMENT.getOrEmpty(EnchantmentHelper.getIdFromNbt(nbtCompound)).ifPresent((enchantment) ->
                tooltip.add(enchantment.getName(EnchantmentHelper.getLevelFromNbt(nbtCompound)))); // add the tooltip line based on this data
        }
    }

}
