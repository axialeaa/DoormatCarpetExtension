package com.axialeaa.doormat.helper.rule;

import net.minecraft.client.MinecraftClient;

/**
 * A class to dynamically store information about the compact enchantment tooltips, bypassing mixin class restrictions and fixing graphical artifacts.
 */
public class EnchantmentCarouselHelper {

    public static int LIST_INDEX = 0;
    public static int LIST_SIZE = 0;

    /**
     * Increments the list index value every second, and wraps back around to 0 when at the end of the list (hence "carousel").
     * @implNote Referencing the gametime via a method that only gets loaded in certain circumstances (in this case hovering over an item) causes some weird flickers. These can be resolved by constantly checking the gametime in {@link MinecraftClient} and quering the output via a global variable.
     */
    public static void onTick(long gameTime) {
        long modTime = gameTime % 20;
        if (modTime == 0)
            if (LIST_INDEX < LIST_SIZE - 1)
                LIST_INDEX++;
            else LIST_INDEX = 0;
    }

}
