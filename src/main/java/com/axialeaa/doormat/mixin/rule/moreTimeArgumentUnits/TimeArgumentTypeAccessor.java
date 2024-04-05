package com.axialeaa.doormat.mixin.rule.moreTimeArgumentUnits;

import it.unimi.dsi.fastutil.objects.Object2IntMap;
import net.minecraft.command.argument.TimeArgumentType;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

@Mixin(TimeArgumentType.class)
public interface TimeArgumentTypeAccessor {

    @Accessor("UNITS")
    static Object2IntMap<String> getUnits() {
        throw new AssertionError();
    }

}
