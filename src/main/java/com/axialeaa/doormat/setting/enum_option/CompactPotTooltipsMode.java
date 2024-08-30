package com.axialeaa.doormat.setting.enum_option;

public enum CompactPotTooltipsMode {

    FALSE, TRUE, IGNORE_BRICKS;

    public boolean isEnabled() {
        return this != FALSE;
    }

}