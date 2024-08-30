package com.axialeaa.doormat.setting.enum_option.function;

import net.minecraft.entity.player.PlayerEntity;
import org.apache.commons.lang3.function.ToBooleanBiFunction;
import java.util.UUID;

public enum DisablePetAttackingMode {

    FALSE ((ownerUUID, player) -> false),
    TRUE  ((ownerUUID, player) -> true),
    OWNED ((ownerUUID, player) -> ownerUUID == player.getUuid());

    private final ToBooleanBiFunction<UUID, PlayerEntity> function;

    DisablePetAttackingMode(ToBooleanBiFunction<UUID, PlayerEntity> function) {
        this.function = function;
    }

    public boolean shouldBypassDamage(UUID ownerUUID, PlayerEntity player) {
        return this.function.applyAsBoolean(ownerUUID, player);
    }

    public boolean isEnabled() {
        return this != FALSE;
    }

}