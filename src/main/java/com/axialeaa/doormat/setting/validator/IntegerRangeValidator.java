package com.axialeaa.doormat.setting.validator;

import carpet.api.settings.CarpetRule;
import carpet.api.settings.Validator;
import net.minecraft.server.command.ServerCommandSource;
import org.jetbrains.annotations.Nullable;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public abstract class IntegerRangeValidator extends Validator<String> {

    abstract int getMin();

    abstract int getMax();

    @Override
    public String validate(@Nullable ServerCommandSource source, CarpetRule<String> changingRule, String newValue, String userInput) {
        Pattern pattern = Pattern.compile("^(\\d+)-(\\d+)$");
        Matcher matcher = pattern.matcher(newValue);

        if (!matcher.matches())
            return null;

        int small = Integer.parseInt(matcher.group(1));
        int large = Integer.parseInt(matcher.group(2));

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
