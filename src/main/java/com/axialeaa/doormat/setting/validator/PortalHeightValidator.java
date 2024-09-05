package com.axialeaa.doormat.setting.validator;

public class PortalHeightValidator extends IntegerRangeValidator {

    @Override
    int getMin() {
        return 1;
    }

    @Override
    int getMax() {
        return 128;
    }

}
