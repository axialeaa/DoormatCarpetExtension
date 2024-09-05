package com.axialeaa.doormat.setting.enum_option;

public enum CryingObsidianPortalFramesMode {

    FALSE, TRUE, CONVERT;

    public boolean isEnabled() {
        return this != FALSE;
    }

}