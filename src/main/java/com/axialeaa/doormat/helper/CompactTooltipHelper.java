package com.axialeaa.doormat.helper;

import net.minecraft.client.MinecraftClient;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.registry.entry.RegistryEntry;
import net.minecraft.registry.tag.EnchantmentTags;
import net.minecraft.text.MutableText;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;

public class CompactTooltipHelper {

    public static int LIST_INDEX = 0;
    public static int LIST_SIZE = 0;

    /**
     * Increments the list index value every second, and wraps back around to 0 when at the end of the list (hence "carousel").
     * @implNote Referencing the gametime via a method that only gets loaded in certain circumstances (in this case hovering over an item) causes some weird flickers. These can be resolved by constantly checking the gametime in {@link MinecraftClient} and quering the output via a global variable.
     */
    public static void onTick(long gameTime) {
        if (gameTime % 20 > 0)
            return;

        if (getNumerator() < getDenominator())
            LIST_INDEX++;
        else LIST_INDEX = 0;
    }

    public static MutableText format(MutableText text) {
        return text.formatted(Formatting.DARK_GRAY);
    }

    public static MutableText format(MutableText text, RegistryEntry<Enchantment> enchantment) {
        return text.formatted(enchantment.isIn(EnchantmentTags.CURSE) ? Formatting.DARK_RED : Formatting.DARK_GRAY);
    }

    public static int getNumerator() {
        return LIST_INDEX + 1;
    }

    public static int getDenominator() {
        return LIST_SIZE;
    }

    public static String getFraction() {
        return "%d / %d".formatted(getNumerator(), getDenominator());
    }

    public static MutableText getFormattedFraction() {
        MutableText fraction = Text.literal(getFraction());
        return format(fraction.append(": "));
    }

}