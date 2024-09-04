package com.axialeaa.doormat.setting.enum_option.function;

import net.minecraft.entity.passive.TameableEntity;
import net.minecraft.entity.player.PlayerEntity;
import org.apache.commons.lang3.function.ToBooleanBiFunction;

public enum DisablePetAttackingMode {

    /**
     * Allows all players to hit all tamed pets.
     */
    FALSE (false),
    /**
     * Disables attacking all tamed pets.
     */
    TRUE (true),
    /**
     * Disables attacking pets that belong to you, but allows you to hit other players' pets.
     */
    OWNED ((player, pet) -> pet.isOwner(player));

    private final ToBooleanBiFunction<PlayerEntity, TameableEntity> function;

    DisablePetAttackingMode(boolean negateDamage) {
        this.function = (player, pet) -> negateDamage;
    }

    DisablePetAttackingMode(ToBooleanBiFunction<PlayerEntity, TameableEntity> function) {
        this.function = function;
    }

    public boolean shouldNegateDamage(PlayerEntity player, TameableEntity pet) {
        return this.function.applyAsBoolean(player, pet);
    }

    public boolean isEnabled() {
        return this != FALSE;
    }

}