package com.axialeaa.doormat.util;

import net.minecraft.util.StringIdentifiable;

import java.util.Locale;

/**
 * This is where the types of updates each redstone component can emit are defined.
 */
public enum UpdateType implements StringIdentifiable {

    NEITHER,
    BLOCK,
    SHAPE,
    BOTH;

    @Override
    public String asString() {
        return toString().toLowerCase(Locale.ROOT);
    }

    public int getFlags() {
        return ordinal();
    }

    public static UpdateType getFromFlags(int flags) {
        return values()[flags];
    }

}