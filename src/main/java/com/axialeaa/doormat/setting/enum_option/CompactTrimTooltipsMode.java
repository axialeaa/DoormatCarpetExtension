package com.axialeaa.doormat.setting.enum_option;

/**
 * @see <a href="https://github.com/axialeaa/DoormatCarpetExtension/wiki/Using-Compact-Tooltip-Rules#armor-trims">Using Compact Tooltip Rules§Armor Trims</a>
 */
public enum CompactTrimTooltipsMode {

    FALSE, TRUE, NO_MATERIAL;

    public boolean isEnabled() {
        return this != FALSE;
    }

}