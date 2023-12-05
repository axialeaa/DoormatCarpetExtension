package com.axialeaa.doormat.util;

import com.axialeaa.doormat.helpers.RedstoneRuleHelper;
import net.minecraft.text.Text;

import java.util.HashMap;
import java.util.Map;

/**
 * All possible redstone components that can be modified using /updatetype, alongside their default values.
 * @see QuasiConnectivityRules
 */
public enum UpdateTypeRules {
    BARREL          ("barrel",              UpdateTypes.SHAPE),
    BELL            ("bell",                UpdateTypes.BOTH),
    COPPER_BULB     ("copper_bulb",         UpdateTypes.BOTH),
    CRAFTER         ("crafter",             UpdateTypes.SHAPE),
    DISPENSER       ("dispenser",           UpdateTypes.SHAPE),
    DOOR            ("door",                UpdateTypes.SHAPE),
    FENCE_GATE      ("fence_gate",          UpdateTypes.SHAPE),
    HOPPER          ("hopper",              UpdateTypes.SHAPE),
    REDSTONE_LAMP   ("redstone_lamp",       UpdateTypes.SHAPE),
    NOTE_BLOCK      ("note_block",          UpdateTypes.BOTH),
    PISTON          ("piston",              UpdateTypes.BOTH),
    RAIL            ("rail",                UpdateTypes.BOTH),
    TNT             ("tnt",                 UpdateTypes.BOTH),
    TRAPDOOR        ("trapdoor",            UpdateTypes.SHAPE);

    private final String key;
    private final UpdateTypes defaultValue;

    UpdateTypeRules(String key, UpdateTypes defaultValue) {
        this.key = key;
        this.defaultValue = defaultValue;
    }

    public UpdateTypes getDefaultValue() {
        return defaultValue;
    }
    public String getKey() {
        return key;
    }

    public Text getFormattedName() {
        return RedstoneRuleHelper.getFormattedComponentName(getKey());
    }

    public static String[] getCommandSuggestions() {
        return RedstoneRuleHelper.getCommandSuggestions(ruleKeys);
    }

    public static final Map<UpdateTypeRules, UpdateTypes> ruleValues = new HashMap<>();
    public static final Map<String, UpdateTypeRules> ruleKeys = new HashMap<>();
    // Define new hashmaps comprised of the component enum entries and their values, and another for the entries' keys.

    static {
        for (UpdateTypeRules component : values()) {
            ruleValues.put(component, component.getDefaultValue());
            ruleKeys.put(component.getKey(), component);
            // Go through the list of components in the enum, and assign "starting"--default--values to the hashmap above,
            //      as well as the keys from the enum itself.
            // The latter will be useful for command autocompletion and text localisation later down the line.
        }
    }

    /**
     * This is where the types of updates each redstone component can emit are defined.
     */
    public enum UpdateTypes {
        NEITHER ("neither", 0),
        BLOCK   ("block",   1),
        SHAPE   ("shape",   2),
        BOTH    ("both",    3);

        private final String key;
        private final int flags;

        public String getKey() {
            return key;
        }
        public int getFlags() {
            return flags;
        }

        UpdateTypes(String key, int flags) {
            this.key = key;
            this.flags = flags;
        }

        // Define a new hashmap comprised of the UpdateType enum entries and their corresponding string keys.
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

