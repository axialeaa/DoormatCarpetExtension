package com.axialeaa.doormat.mixin.rule.compactTooltips;

import com.axialeaa.doormat.DoormatSettings;
import com.axialeaa.doormat.helper.CompactTooltipHelper;
import net.minecraft.component.type.ItemEnchantmentsComponent;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.item.Item;
import net.minecraft.item.tooltip.TooltipType;
import net.minecraft.registry.entry.RegistryEntry;
import net.minecraft.text.MutableText;
import net.minecraft.text.Text;
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
    @Shadow public abstract Set<RegistryEntry<Enchantment>> getEnchantments();
    @Shadow public abstract int getLevel(RegistryEntry<Enchantment> enchantment);

    @Shadow public abstract boolean isEmpty();

    /**
     * Cycles the displayed line using the logic in {@link CompactTooltipHelper}.
     */
    @Inject(method = "appendTooltip", at = @At(value = "INVOKE", target = "Lnet/minecraft/item/Item$TooltipContext;getRegistryLookup()Lnet/minecraft/registry/RegistryWrapper$WrapperLookup;", shift = At.Shift.BEFORE), cancellable = true)
    private void appendTooltipCarousel(Item.TooltipContext context, Consumer<Text> tooltip, TooltipType type, CallbackInfo ci) {
        if (!DoormatSettings.compactEnchantTooltips.enabled() || this.isEmpty())
            return;

        CompactTooltipHelper.LIST_SIZE = this.getSize();

        RegistryEntry<Enchantment> enchantment = this.getEnchantments().stream().toList().get(CompactTooltipHelper.LIST_INDEX);
        Text name = Enchantment.getName(enchantment, this.getLevel(enchantment));

        if (CompactTooltipHelper.LIST_SIZE == 1) {
            tooltip.accept(name);
            ci.cancel();
        }

        switch (DoormatSettings.compactEnchantTooltips) {
            case TRUE -> tooltip.accept(CompactTooltipHelper.getFormattedFraction().append(name));
            case BAR -> {
                tooltip.accept(name);
                MutableText mutableText = Text.empty();

                // Constructs the bar a number of characters long equal to the length of the enchantment list,
                // filling in the square if the enchantment at that ordinal is currently displayed by the carousel
                for (int i = 1; i <= CompactTooltipHelper.getDenominator(); ++i) {
                    RegistryEntry<Enchantment> barEnchantment = this.getEnchantments().stream().toList().get(i - 1);
                    String dot = i == CompactTooltipHelper.getNumerator() ? "■" : "□";

                    mutableText.append(CompactTooltipHelper.format(Text.literal(dot), barEnchantment));
                }

                tooltip.accept(CompactTooltipHelper.format(mutableText.append(" (" + CompactTooltipHelper.getFraction() + ")")));
            }
        }

        ci.cancel();
    }

}