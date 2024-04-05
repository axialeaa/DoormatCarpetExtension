package com.axialeaa.doormat.mixin.rule.disablePetAttacking;

import com.axialeaa.doormat.DoormatSettings;
import com.axialeaa.doormat.mixin.extensibility.EntityMixin;
import net.minecraft.entity.Entity;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.entity.passive.TameableEntity;
import net.minecraft.entity.player.PlayerEntity;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.Optional;
import java.util.UUID;

@Mixin(TameableEntity.class)
public abstract class TameableEntityMixin extends EntityMixin {

    @Shadow @Nullable public abstract UUID getOwnerUuid();

    @SuppressWarnings("OptionalUsedAsFieldOrParameterType")
    @Unique
    private boolean fitsCriteria(Optional<UUID> optional, PlayerEntity player) {
        return switch (DoormatSettings.disablePetAttacking) {
            case FALSE -> false;
            case TRUE -> true;
            case OWNED -> optional.isPresent() && player.getUuid().equals(optional.get());
            // if the owner uuid exists (optional.isPresent()), that means the mob is tamed
        };
    }

    /**
     * Disables entity damage if the rule is enabled and the instigator fits the criteria for the rule setting.
     * @param source the type of damage to be dealt to the entity
     * @param amount the amount of damage to be dealt to the entity
     * @param cir returnable callback info parameter, since this method is instantiated from the original handler method in {@link EntityMixin}
     */
    @Override
    public void damageImpl(DamageSource source, float amount, CallbackInfoReturnable<Boolean> cir) {
        Entity attacker = source.getAttacker();
        if (attacker instanceof PlayerEntity player) {
            Optional<UUID> optional = Optional.ofNullable(this.getOwnerUuid());
            if (fitsCriteria(optional, player))
                cir.setReturnValue(false);
        }
    }

}
