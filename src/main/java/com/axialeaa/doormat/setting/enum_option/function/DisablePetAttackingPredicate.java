package com.axialeaa.doormat.setting.enum_option.function;

import net.minecraft.entity.passive.TameableEntity;
import net.minecraft.entity.player.PlayerEntity;

@FunctionalInterface
public interface DisablePetAttackingPredicate {

    boolean shouldNegateDamage(PlayerEntity player, TameableEntity pet);

}