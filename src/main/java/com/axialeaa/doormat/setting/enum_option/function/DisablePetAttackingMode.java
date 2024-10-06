package com.axialeaa.doormat.setting.enum_option.function;

import net.minecraft.entity.passive.TameableEntity;
import net.minecraft.entity.player.PlayerEntity;

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

    private final DisablePetAttackingPredicate predicate;

    DisablePetAttackingMode(boolean negateDamage) {
        this((player, pet) -> negateDamage);
    }

    DisablePetAttackingMode(DisablePetAttackingPredicate predicate) {
        this.predicate = predicate;
    }

    public boolean shouldNegateDamage(PlayerEntity player, TameableEntity pet) {
        return this.predicate.shouldNegateDamage(player, pet);
    }

    public boolean isEnabled() {
        return this != FALSE;
    }

    @FunctionalInterface
    public interface DisablePetAttackingPredicate {

        boolean shouldNegateDamage(PlayerEntity player, TameableEntity pet);

    }

}