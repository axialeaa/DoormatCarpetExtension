package com.axialeaa.doormat.settings.enum_options.function;

import java.util.function.Function;

public enum PiglinBlockGuardingMode {

    FALSE       (blockOpen -> false),
    INTERACTED  (blockOpen -> true),
    OPEN        (blockOpen -> blockOpen),
    BROKEN      (blockOpen -> !blockOpen);

    private final Function<Boolean, Boolean> function;

    PiglinBlockGuardingMode(Function<Boolean, Boolean> function) {
        this.function = function;
    }

    public boolean shouldAnger(boolean blockOpen) {
        return this.function.apply(blockOpen);
    }

}