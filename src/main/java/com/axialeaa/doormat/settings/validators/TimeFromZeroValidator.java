package com.axialeaa.doormat.settings.validators;

public class TimeFromZeroValidator extends TimeValidator {

    @Override
    public int getMin() {
        return 0;
    }

}