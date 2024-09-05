package com.axialeaa.doormat.setting.validator;

import carpet.api.settings.CarpetRule;
import carpet.api.settings.Validator;
import net.minecraft.server.command.ServerCommandSource;
import org.jetbrains.annotations.Nullable;

public abstract class IntegerRangeValidator extends Validator<String> {

    abstract int getMin();

    abstract int getMax();

    @Override
    public String validate(@Nullable ServerCommandSource source, CarpetRule<String> changingRule, String newValue, String userInput) {
        if (!newValue.matches("^[0-9-]+$"))
            return null;

        String[] split = newValue.split("-");

        if (split.length != 2)
            return null;

        int min = Integer.parseInt(split[0]);
        int max = Integer.parseInt(split[1]);

        if (min < this.getMin() || max > this.getMax() || min > max)
            return null;

        return newValue;
    }

    public static int getMinFromString(String string) {
        String[] split = string.split("-");
        return Integer.parseInt(split[0]);
    }

    public static int getMaxFromString(String string) {
        String[] split = string.split("-");
        return Integer.parseInt(split[1]);
    }

    @Override
    public String description() {
        return "You must choose values within the range %s-%s, smallest to largest".formatted(this.getMin(), this.getMax());
    }

}
