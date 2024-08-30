package com.axialeaa.doormat.setting.validator;

public class TimeFromZeroValidator extends TimeValidator {

    @Override
    public int getMin() {
        return 0;
    }

}