package com.axialeaa.doormat.mixin.rule.compactEnchantTooltips;

import com.axialeaa.doormat.DoormatSettings;
import com.axialeaa.doormat.helper.EnchantmentCarouselHelper;
import net.minecraft.client.item.TooltipType;
import net.minecraft.component.type.ItemEnchantmentsComponent;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.item.Item;
import net.minecraft.registry.entry.RegistryEntry;
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
    @Shadow public abstract int getLevel(Enchantment enchantment);

    @Shadow public abstract Set<RegistryEntry<Enchantment>> getEnchantments();

    /**
     * Cycles the displayed line using the logic in {@link EnchantmentCarouselHelper}.
     */
    @Inject(method = "appendTooltip", at = @At(value = "INVOKE", target = "Lit/unimi/dsi/fastutil/objects/Object2IntOpenHashMap;object2IntEntrySet()Lit/unimi/dsi/fastutil/objects/Object2IntMap$FastEntrySet;", shift = At.Shift.BEFORE), cancellable = true, remap = false)
    private void cycleLine(Item.TooltipContext context, Consumer<Text> tooltip, TooltipType type, CallbackInfo ci) {
        EnchantmentCarouselHelper.LIST_SIZE = this.getSize();
        if (DoormatSettings.compactEnchantTooltips) {
            RegistryEntry<Enchantment> registryEntry = null;

            try {
                registryEntry = this.getEnchantments().stream().toList().get(EnchantmentCarouselHelper.LIST_INDEX);
            }
            catch (ArrayIndexOutOfBoundsException ignored) {}

            if (registryEntry != null) {
                Enchantment enchantment = registryEntry.value();
                tooltip.accept(enchantment.getName(this.getLevel(enchantment)));
                ci.cancel();
            }
        }
    }

}
