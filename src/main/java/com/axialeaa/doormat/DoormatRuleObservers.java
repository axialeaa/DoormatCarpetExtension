package com.axialeaa.doormat;

import carpet.api.settings.CarpetRule;
import com.axialeaa.doormat.mixin.rule.moreTimeArgumentUnits.TimeArgumentTypeAccessor;
import it.unimi.dsi.fastutil.objects.Object2IntMap;

import java.util.Objects;

public class DoormatRuleObservers {

    public static void moreTimeArgumentUnits(CarpetRule<?> currentRuleState) {
        String name = currentRuleState.name();
        Object value = currentRuleState.value();

        if (Objects.equals(name, "moreTimeArgumentUnits")) {
            Object2IntMap<String> units = TimeArgumentTypeAccessor.getUnits();

            if ((boolean) value) {
                units.putIfAbsent("m", 1200);
                units.putIfAbsent("h", 72000);
            }
            else {
                units.removeInt("m");
                units.removeInt("h");
            }
        }
    }

}
