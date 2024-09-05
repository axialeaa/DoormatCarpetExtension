package com.axialeaa.doormat.setting.validator;

public class PortalSizeValidator extends IntegerRangeValidator {

    @Override
    int getMin() {
        return 1;
    }

    @Override
    int getMax() {
        return 128; // Subject to change
    }

}
