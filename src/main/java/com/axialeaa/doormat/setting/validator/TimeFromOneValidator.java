package com.axialeaa.doormat.setting.validator;

public class TimeFromOneValidator extends TimeValidator {

    @Override
    public int getMin() {
        return 1;
    }

}