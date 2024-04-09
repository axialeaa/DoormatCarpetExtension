package com.axialeaa.doormat.util;

import net.minecraft.util.StringIdentifiable;

import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

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

    public static final Map<Integer, UpdateType> FLAGS_TO_UPDATE_TYPE = new HashMap<>();

    static {
        for (UpdateType updateType : values())
            FLAGS_TO_UPDATE_TYPE.put(updateType.getFlags(), updateType);
    }

}