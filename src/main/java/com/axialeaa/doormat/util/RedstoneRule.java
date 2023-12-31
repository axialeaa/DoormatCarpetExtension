package com.axialeaa.doormat.util;

import com.axialeaa.doormat.helpers.RedstoneRuleHelper;

import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

import static com.axialeaa.doormat.util.RedstoneRule.UpdateTypes.*;

/**
 * All possible redstone components that can be modified using /quasiconnectivity and /updatetype, alongside their default values for both.
 */
public enum RedstoneRule {

    BARREL          ("Barrel",                   false,  SHAPE),
    BELL            ("Bell",                     false,  BOTH),
    COMMAND_BLOCK   ("Command Block",            false),
    COPPER_BULB     ("Copper Bulb",              false,  BOTH),
    CRAFTER         ("Crafter",                  false,  SHAPE),
    DISPENSER       ("Dispenser and Dropper",    true,   SHAPE),
    DOOR            ("Door",                     false,  SHAPE),
    FENCE_GATE      ("Fence Gate",               false,  SHAPE),
    HOPPER          ("Hopper",                   false,  SHAPE),
    REDSTONE_LAMP   ("Redstone Lamp",            false,  SHAPE),
    NOTE_BLOCK      ("Note Block",               false,  BOTH),
    PISTON          ("Piston and Sticky Piston", true,   BOTH),
    RAIL            ("Rail",                     false,  BOTH),
    STRUCTURE_BLOCK ("Structure Block",          false),
    TNT             ("TNT",                      false,  BOTH),
    TRAPDOOR        ("Trapdoor",                 false,  SHAPE);

    private final String prettyName;
    private final boolean defaultQCValue;
    private final UpdateTypes defaultUpdateTypeValue;

    RedstoneRule(String prettyName, boolean defaultQCValue, UpdateTypes defaultUpdateTypeValue) {
        this.prettyName = prettyName;
        this.defaultQCValue = defaultQCValue;
        this.defaultUpdateTypeValue = defaultUpdateTypeValue;
    }

    RedstoneRule(String prettyName, boolean defaultQCValue) {
        this.prettyName = prettyName;
        this.defaultQCValue = defaultQCValue;
        this.defaultUpdateTypeValue = null;
    }

    public String getKey() {
        return toString().toLowerCase(Locale.ROOT);
        // Returns the name of the constant converted to lower snake case (STRUCTURE_BLOCK -> structure_block).
    }

    public String getPrettyName() {
        return prettyName;
    }

    public boolean getDefaultQCValue() {
        return defaultQCValue;
    }

    public UpdateTypes getDefaultUpdateTypeValue() {
        return defaultUpdateTypeValue;
    }

    public static String[] getQCCommandSuggestions() {
        return RedstoneRuleHelper.getCommandSuggestions(qcKeys);
    }

    public static String[] getUpdateTypeCommandSuggestions() {
        return RedstoneRuleHelper.getCommandSuggestions(updateTypeKeys);
    }

    public static final Map<RedstoneRule, Boolean> qcValues = new HashMap<>();
    public static final Map<String, RedstoneRule> qcKeys = new HashMap<>();

    public static final Map<RedstoneRule, UpdateTypes> updateTypeValues = new HashMap<>();
    public static final Map<String, RedstoneRule> updateTypeKeys = new HashMap<>();
    // Define new hashmaps comprised of the component enum constants and their values, and another for the constants' keys.

    /**
     * This is where the types of updates each redstone component can emit are defined.
     */
    public enum UpdateTypes {
        NEITHER, BLOCK, SHAPE, BOTH;

        public String getKey() {
            return toString().toLowerCase(Locale.ROOT);
            // Returns the name of the constant converted to lower snake case (NEITHER -> neither).
        }

        // Define a new hashmap comprised of the UpdateType enum constants and their corresponding string keys.
        // This is necessary because commands cannot take in enum arguments.
        // Instead, pass in a string which gets converted to the enum option for calculations!
        public static final Map<String, UpdateTypes> keys = new HashMap<>();

        // Create an array out of all the strings in the hashmap used for command autocompletion.
        public static String[] getCommandSuggestions() {
            return RedstoneRuleHelper.getCommandSuggestions(keys);
        }

        static {
            // Create each hashmap value and leave it at that: no more "putting" required.
            for (UpdateTypes updateType : values())
                keys.put(updateType.getKey(), updateType);
        }

    }

}

