package com.axialeaa.doormat.settings.validators;

public class TimeFromOneValidator extends TimeValidator {

    @Override
    public int getMin() {
        return 1;
    }

}