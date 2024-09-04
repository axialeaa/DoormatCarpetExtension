package com.axialeaa.doormat.setting.enum_option.function;

import net.minecraft.entity.player.PlayerEntity;
import org.apache.commons.lang3.function.ToBooleanBiFunction;

public enum DisablePiglinBlockGuardingMode {

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

    private final ToBooleanBiFunction<PlayerEntity, Boolean> function;

    DisablePiglinBlockGuardingMode(boolean negateAnger) {
        this.function = (player, blockOpen) -> negateAnger;
    }

    DisablePiglinBlockGuardingMode(ToBooleanBiFunction<PlayerEntity, Boolean> function) {
        this.function = function;
    }

    public boolean shouldNegateAnger(PlayerEntity player, boolean blockOpen) {
        return this.function.applyAsBoolean(player, blockOpen);
    }

}