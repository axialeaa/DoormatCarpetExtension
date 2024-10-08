package com.axialeaa.doormat.setting.enum_option;

import com.axialeaa.doormat.setting.enum_option.function.DisablePiglinBlockGuardingPredicate;
import net.minecraft.entity.player.PlayerEntity;

public enum DisablePiglinBlockGuardingMode implements DisablePiglinBlockGuardingPredicate {

    /**
     * Allows piglins to be aggravated by players interacting with gold blocks or loot containers in any way.
     */
    FALSE (false),
    /**
     * Completely disables piglin guarding.
     */
    TRUE (true),
    /**
     * Allows players to safely open loot containers, but not destroy them.
     */
    OPEN ((player, blockOpen) -> blockOpen),
    /**
     * Allows players to safely interact with blocks only when they're sneaking.
     */
    SNEAKING ((player, blockOpen) -> player.isSneaking());

    private final DisablePiglinBlockGuardingPredicate predicate;

    DisablePiglinBlockGuardingMode(boolean negateAnger) {
        this((player, blockOpen) -> negateAnger);
    }

    DisablePiglinBlockGuardingMode(DisablePiglinBlockGuardingPredicate predicate) {
        this.predicate = predicate;
    }

    @Override
    public boolean shouldNegateAnger(PlayerEntity player, boolean blockOpen) {
        return this.predicate.shouldNegateAnger(player, blockOpen);
    }

}