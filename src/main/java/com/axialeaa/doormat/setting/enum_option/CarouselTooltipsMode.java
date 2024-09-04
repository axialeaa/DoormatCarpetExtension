package com.axialeaa.doormat.setting.enum_option;

/**
 * @see <a href="https://github.com/axialeaa/DoormatCarpetExtension/wiki/Using-Compact-Tooltip-Rules#enchantments">Using Compact Tooltip Rules§Enchantments</a>
 * @see <a href="https://github.com/axialeaa/DoormatCarpetExtension/wiki/Using-Compact-Tooltip-Rules#banners">Using Compact Tooltip Rules§Banners</a>
 */
public enum CarouselTooltipsMode {

    FALSE, TRUE, BAR;

    public boolean isEnabled() {
        return this != FALSE;
    }

}