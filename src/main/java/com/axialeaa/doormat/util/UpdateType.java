package com.axialeaa.doormat.util;

import java.util.Locale;

/**
 * This is where the types of updates each redstone component can emit are defined.
 */
public enum UpdateType {

    NEITHER,
    BLOCK,
    SHAPE,
    BOTH;

    @Override
    public String toString() {
        return this.name().toLowerCase(Locale.ROOT);
    }

    public int getFlags() {
        return this.ordinal();
    }

    public static UpdateType getFromFlags(int flags) {
        return values()[flags];
    }

}