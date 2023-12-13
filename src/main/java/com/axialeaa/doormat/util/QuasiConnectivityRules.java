package com.axialeaa.doormat.util;

import com.axialeaa.doormat.helpers.RedstoneRuleHelper;

import java.util.HashMap;
import java.util.Map;

/**
 * All possible redstone components that can be modified using /quasiconnectivity, alongside their default values. This class is significantly smaller and easier to understand due to the use of standard boolean options instead of a brand new enum.
 * @see UpdateTypeRules
 */
public enum QuasiConnectivityRules {
    BARREL          ("barrel",          "Barrel",                   false),
    BELL            ("bell",            "Bell",                     false),
    COPPER_BULB     ("copper_bulb",     "Copper Bulb",              false),
    COMMAND_BLOCK   ("command_block",   "Command Block",            false),
    CRAFTER         ("crafter",         "Crafter",                  false),
    DISPENSER       ("dispenser",       "Dispenser and Dropper",    true),
    DOOR            ("door",            "Door",                     false),
    FENCE_GATE      ("fence_gate",      "Fence Gate",               false),
    HOPPER          ("hopper",          "Hopper",                   false),
    REDSTONE_LAMP   ("redstone_lamp",   "Redstone Lamp",            false),
    NOTE_BLOCK      ("note_block",      "Note Block",               false),
    PISTON          ("piston",          "Piston and Sticky Piston", true),
    RAIL            ("rail",            "Rail",                     false),
    STRUCTURE_BLOCK ("structure_block", "Structure Block",          false),
    TNT             ("tnt",             "TNT",                      false),
    TRAPDOOR        ("trapdoor",        "Trapdoor",                 false);

    private final String key;
    private final String prettyName;
    private final boolean defaultValue;

    public String getKey() {
        return key;
    }
    public String getPrettyName() {
        return prettyName;
    }
    public boolean getDefaultValue() {
        return defaultValue;
    }

    QuasiConnectivityRules(String key, String prettyName, boolean defaultValue) {
        this.key = key;
        this.prettyName = prettyName;
        this.defaultValue = defaultValue;
    }

    public static String[] getCommandSuggestions() {
        return RedstoneRuleHelper.getCommandSuggestions(ruleKeys);
    }

    public static final Map<QuasiConnectivityRules, Boolean> ruleValues = new HashMap<>();
    public static final Map<String, QuasiConnectivityRules> ruleKeys = new HashMap<>();
    // Define new hashmaps comprised of the component enum entries and their boolean values, and another for the entries' keys.

    static {
        for (QuasiConnectivityRules component : values())
            ruleKeys.put(component.getKey(), component);
        // Go through the list of components in the enum, and assign their keys to the hashmap above,
        // This will be useful for command autocompletion later down the line.
    }

}