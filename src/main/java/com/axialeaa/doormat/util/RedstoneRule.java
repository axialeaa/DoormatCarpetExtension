package com.axialeaa.doormat.util;

import carpet.CarpetServer;
import com.axialeaa.doormat.DoormatServer;
import com.axialeaa.doormat.helpers.RedstoneRuleHelper;

import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

/**
 * All possible redstone components that can be modified using /quasiconnectivity and /updatetype, alongside their default values for both.
 */
public enum RedstoneRule {

    BARREL          ("Barrel",                   false,  UpdateTypes.SHAPE),
    BELL            ("Bell",                     false,  UpdateTypes.BOTH),
    COMMAND_BLOCK   ("Command Block",            false,  null),
    COPPER_BULB     ("Copper Bulb",              false,  UpdateTypes.BOTH),
    CRAFTER         ("Crafter",                  false,  UpdateTypes.SHAPE),
    DISPENSER       ("Dispenser and Dropper",    true,   UpdateTypes.SHAPE),
    DOOR            ("Door",                     false,  UpdateTypes.SHAPE),
    FENCE_GATE      ("Fence Gate",               false,  UpdateTypes.SHAPE),
    HOPPER          ("Hopper",                   false,  UpdateTypes.SHAPE),
    REDSTONE_LAMP   ("Redstone Lamp",            false,  UpdateTypes.SHAPE),
    NOTE_BLOCK      ("Note Block",               false,  UpdateTypes.BOTH),
    PISTON          ("Piston and Sticky Piston", true,   UpdateTypes.BOTH),
    RAIL            ("Rail",                     false,  UpdateTypes.BOTH),
    STRUCTURE_BLOCK ("Structure Block",          false,  null),
    TNT             ("TNT",                      false,  UpdateTypes.BOTH),
    TRAPDOOR        ("Trapdoor",                 false,  UpdateTypes.SHAPE);

    private final String prettyName;
    private final boolean defaultQCValue;
    private final UpdateTypes defaultUpdateTypeValue;

    RedstoneRule(String prettyName, boolean defaultQCValue, UpdateTypes defaultUpdateTypeValue) {
        this.prettyName = prettyName;
        this.defaultQCValue = defaultQCValue;
        this.defaultUpdateTypeValue = defaultUpdateTypeValue;
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

    static {
        for (RedstoneRule component : values()) {
            if (!DoormatServer.hasExperimentalDatapack(CarpetServer.minecraft_server) && (component == CRAFTER || component == COPPER_BULB))
                continue;
            qcKeys.put(component.getKey(), component);
            if (component.getDefaultUpdateTypeValue() != null)
                updateTypeKeys.put(component.getKey(), component);
        }
        // Go through the list of components in the enum, and assign their keys to the hashmaps above,
        // This will be useful for command autocompletion later down the line.
    }

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

