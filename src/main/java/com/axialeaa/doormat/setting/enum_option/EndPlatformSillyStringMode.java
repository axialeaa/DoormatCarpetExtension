package com.axialeaa.doormat.setting.enum_option;

public enum EndPlatformSillyStringMode {

    FALSE, TRUE, NO_DROPS;

    public boolean isEnabled() {
        return this != FALSE;
    }

}