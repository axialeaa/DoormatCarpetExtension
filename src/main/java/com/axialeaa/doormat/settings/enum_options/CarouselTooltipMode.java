package com.axialeaa.doormat.settings.enum_options;

public enum CarouselTooltipMode {

    FALSE, TRUE, BAR;

    public boolean isEnabled() {
        return this != FALSE;
    }

}