package com.axialeaa.doormat.util;

import com.axialeaa.doormat.helpers.RedstoneRuleHelper;
import net.minecraft.text.Text;

import java.util.HashMap;
import java.util.Map;

/**
 * All possible redstone components that can be modified using /quasiconnectivity, alongside their default values. This class is significantly smaller and easier to understand due to the use of standard boolean options instead of a brand new enum.
 * @see UpdateTypeRules
 */
public enum QuasiConnectivityRules {
    BARREL          ("barrel",              false),
    BELL            ("bell",                false),
    COPPER_BULB     ("copper_bulb",         false),
    COMMAND_BLOCK   ("command_block",       false),
    CRAFTER         ("crafter",             false),
    DISPENSER       ("dispenser",           true),
    DOOR            ("door",                false),
    FENCE_GATE      ("fence_gate",          false),
    HOPPER          ("hopper",              false),
    REDSTONE_LAMP   ("redstone_lamp",       false),
    NOTE_BLOCK      ("note_block",          false),
    PISTON          ("piston",              true),
    RAIL            ("rail",                false),
    STRUCTURE_BLOCK ("structure_block",     false),
    TNT             ("tnt",                 false),
    TRAPDOOR        ("trapdoor",            false);

    private final String key;
    private final boolean defaultValue;

    public boolean getDefaultValue() {
        return defaultValue;
    }
    public String getKey() {
        return key;
    }

    QuasiConnectivityRules(String key, boolean defaultValue) {
        this.key = key;
        this.defaultValue = defaultValue;
    }

    public Text getFormattedName() {
        return RedstoneRuleHelper.getFormattedComponentName(getKey());
    }

    public static String[] getCommandSuggestions() {
        return RedstoneRuleHelper.getCommandSuggestions(ruleKeys);
    }

    public static final Map<QuasiConnectivityRules, Boolean> ruleValues = new HashMap<>();
    public static final Map<String, QuasiConnectivityRules> ruleKeys = new HashMap<>();
    // Define new hashmaps comprised of the component enum entries and their boolean values, and another for the entries' keys.

    static {
        for (QuasiConnectivityRules component : values()) {
            ruleValues.put(component, component.getDefaultValue());
            ruleKeys.put(component.getKey(), component);
            // Go through the list of components in the enum, and assign "starting"--default--values to the hashmap above,
            //      as well as the keys from the enum itself.
            // The latter will be useful for command autocompletion and text localisation later down the line.
        }
    }

}