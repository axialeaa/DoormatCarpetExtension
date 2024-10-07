package com.axialeaa.doormat.setting.enum_option.function;

import net.minecraft.entity.player.PlayerEntity;

@FunctionalInterface
public interface DisablePiglinBlockGuardingPredicate {

    boolean shouldNegateAnger(PlayerEntity player, boolean blockOpen);

}