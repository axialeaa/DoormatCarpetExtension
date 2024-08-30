package com.axialeaa.doormat.setting.enum_option;

public enum CarouselTooltipsMode {

    FALSE, TRUE, BAR;

    public boolean isEnabled() {
        return this != FALSE;
    }

}