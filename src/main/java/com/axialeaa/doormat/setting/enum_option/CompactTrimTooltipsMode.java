package com.axialeaa.doormat.setting.enum_option;

public enum CompactTrimTooltipsMode {

    FALSE, TRUE, ONLY_PATTERN;

    public boolean isEnabled() {
        return this != FALSE;
    }

}