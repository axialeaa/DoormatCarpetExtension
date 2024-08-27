package com.axialeaa.doormat.settings.enum_options;

public enum TrimTooltipMode {

    FALSE, TRUE, ONLY_PATTERN;

    public boolean isEnabled() {
        return this != FALSE;
    }

}