package com.axialeaa.doormat.mixin.rule.compactEnchantTooltips;

import com.axialeaa.doormat.DoormatSettings;
import com.axialeaa.doormat.helper.EnchantmentCarouselHelper;
import net.minecraft.client.item.TooltipType;
import net.minecraft.component.type.ItemEnchantmentsComponent;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.item.Item;
import net.minecraft.registry.entry.RegistryEntry;
import net.minecraft.screen.ScreenTexts;
import net.minecraft.text.MutableText;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.Set;
import java.util.function.Consumer;

@Mixin(ItemEnchantmentsComponent.class)
public abstract class ItemEnchantmentsComponentMixin {

    @Shadow public abstract int getSize();
    @Shadow public abstract int getLevel(Enchantment enchantment);

    @Shadow public abstract Set<RegistryEntry<Enchantment>> getEnchantments();

    /**
     * Cycles the displayed line using the logic in {@link EnchantmentCarouselHelper}.
     */
    @Inject(method = "appendTooltip", at = @At(value = "INVOKE", target = "Lnet/minecraft/item/Item$TooltipContext;getRegistryLookup()Lnet/minecraft/registry/RegistryWrapper$WrapperLookup;", shift = At.Shift.BEFORE), cancellable = true)
    private void cycleLine(Item.TooltipContext context, Consumer<Text> tooltip, TooltipType type, CallbackInfo ci) {
        EnchantmentCarouselHelper.LIST_SIZE = this.getSize();

        if (DoormatSettings.compactEnchantTooltips.enabled() && this.getSize() > 0) {
            RegistryEntry<Enchantment> registryEntry = this.getEnchantments().stream().toList().get(EnchantmentCarouselHelper.LIST_INDEX);

            Enchantment enchantment = registryEntry.value();
            Text name = enchantment.getName(this.getLevel(enchantment));

            if (this.getSize() == 1)
                tooltip.accept(name);
            else {
                int numerator = EnchantmentCarouselHelper.LIST_INDEX + 1; // Requires adding 1 because the index is zero-based
                int denominator = this.getSize();

                MutableText fraction = (MutableText) Text.of(numerator + "/" + denominator);

                if (DoormatSettings.compactEnchantTooltips == DoormatSettings.EnchantTooltipMode.TRUE) {
                    MutableText result = fraction.append(":").formatted(Formatting.DARK_GRAY).append(ScreenTexts.SPACE);

                    tooltip.accept(result.append(name));
                }
                else if (DoormatSettings.compactEnchantTooltips == DoormatSettings.EnchantTooltipMode.BAR) {
                    tooltip.accept(name);
                    MutableText mutableText = Text.empty();

                    // Constructs the bar a number of characters long equal to the length of the enchantment list,
                    // filling in the square if the enchantment at that ordinal is currently displayed by the carousel
                    for (int i = 1; i <= denominator; ++i) {
                        RegistryEntry<Enchantment> barEntry = this.getEnchantments().stream().toList().get(i - 1);

                        Enchantment barValue = barEntry.value();
                        String dot = i == numerator ? "■" : "□";

                        mutableText.append(Text.literal(dot).formatted(barValue.isCursed() ? Formatting.DARK_RED : Formatting.DARK_GRAY));
                    }

                    tooltip.accept(mutableText.append(ScreenTexts.SPACE).append("(" + fraction.getString() + ")").formatted(Formatting.DARK_GRAY));
                }
            }

            ci.cancel();
        }
    }

}