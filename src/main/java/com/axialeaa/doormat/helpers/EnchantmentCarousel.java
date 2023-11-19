package com.axialeaa.doormat.helpers;

import com.axialeaa.doormat.mixin.rules.compactEnchantTooltips.ItemStackMixin;
import net.minecraft.client.MinecraftClient;

/**
 * A class to dynamically store information about the compact enchantment tooltips, bypassing mixin class restrictions and fixing graphical artifacts.
 */
public class EnchantmentCarousel {

    /**
     * The value of listIndex is got in {@link ItemStackMixin}, and listSize is set in the same place.
     */
    public static int listIndex = 0;
    public static int listSize = 0;

    /**
     * Increments the list index value every second, and wraps back around to 0 when at the end of the list (hence "carousel").
     * @implNote Referencing the gametime via a method that only gets loaded in certain circumstances (in this case hovering over an item) causes some weird flickers. These can be resolved by constantly checking the gametime in {@link MinecraftClient} and quering the output via a global variable.
     */
    public static void tick(long gameTime) {
        long modTime = gameTime % 20;
        if (modTime == 0)
            if (listIndex < listSize - 1)
                listIndex++;
            else listIndex = 0;
    }

}
