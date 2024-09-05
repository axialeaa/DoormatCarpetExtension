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

        int small = Integer.parseInt(split[0]);
        int large = Integer.parseInt(split[1]);

        if (small < this.getMin() || large > this.getMax() || small > large)
            return null;

        return newValue;
    }

    /**
     * @param string The string input, consisting of two integers separated by a dash, e.g. {@code "1-20"}.
     * @return The integer to the left of the dash.
     */
    public static int getSmall(String string) {
        String[] split = string.split("-");
        return Integer.parseInt(split[0]);
    }

    /**
     * @param string The string input, consisting of two integers separated by a dash, e.g. {@code "1-20"}.
     * @return The integer to the right of the dash.
     */
    public static int getLarge(String string) {
        String[] split = string.split("-");
        return Integer.parseInt(split[1]);
    }

    @Override
    public String description() {
        return "You must choose values within the range %s-%s, smallest to largest".formatted(this.getMin(), this.getMax());
    }

}
