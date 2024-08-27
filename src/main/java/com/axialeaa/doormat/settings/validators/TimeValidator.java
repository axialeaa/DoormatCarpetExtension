package com.axialeaa.doormat.settings.validators;

import carpet.api.settings.CarpetRule;
import carpet.api.settings.Validator;
import net.minecraft.server.command.ServerCommandSource;

public abstract class TimeValidator extends Validator<Integer> {

    public abstract int getMin();

    @Override
    public Integer validate(ServerCommandSource source, CarpetRule<Integer> currentRule, Integer newValue, String typedString) {
        return (newValue >= this.getMin() && newValue <= 1200) ? newValue : null;
    }

    @Override
    public String description() {
        return "You must choose a value from %s to 1200".formatted(this.getMin());
    }

}