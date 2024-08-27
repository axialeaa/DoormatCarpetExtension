package com.axialeaa.doormat.settings.enum_options;

public enum SillyStringMode {

    FALSE, TRUE, DELETE_STRING;

    public boolean isEnabled() {
        return this != FALSE;
    }

}