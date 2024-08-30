package com.axialeaa.doormat.setting.enum_option;

public enum EndPlatformSillyStringMode {

    FALSE, TRUE, DELETE_STRING;

    public boolean isEnabled() {
        return this != FALSE;
    }

}