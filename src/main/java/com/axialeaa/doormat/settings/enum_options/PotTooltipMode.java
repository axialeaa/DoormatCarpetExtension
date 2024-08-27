package com.axialeaa.doormat.settings.enum_options;

public enum PotTooltipMode {

    FALSE, TRUE, IGNORE_BRICKS;

    public boolean isEnabled() {
        return this != FALSE;
    }

}