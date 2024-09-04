package com.axialeaa.doormat.setting.enum_option;

/**
 * @see <a href="https://github.com/axialeaa/DoormatCarpetExtension/wiki/Using-Compact-Tooltip-Rules#decorated-pots">Using Compact Tooltip Rules§Decorated Pots</a>
 */
public enum CompactPotTooltipsMode {

    FALSE, TRUE, IGNORE_BRICKS;

    public boolean isEnabled() {
        return this != FALSE;
    }

}