package com.axialeaa.doormat.mixin.rule.disablePetAttacking;

import com.axialeaa.doormat.mixin.impl.EntityImplMixin;
import com.axialeaa.doormat.settings.DoormatSettings;
import net.minecraft.entity.Entity;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.entity.passive.TameableEntity;
import net.minecraft.entity.player.PlayerEntity;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import java.util.Optional;
import java.util.UUID;

@Mixin(TameableEntity.class)
public abstract class TameableEntityMixin extends EntityImplMixin {

    @Shadow @Nullable public abstract UUID getOwnerUuid();

    /**
     * Disables entity damage if the rule is enabled and the instigator fits the criteria for the rule setting.
     * @param source the entries of damage to be dealt to the entity
     * @param amount the amount of damage to be dealt to the entity
     * @param cir returnable callback info parameter, since this method is instantiated from the original handler method in {@link EntityImplMixin}
     */
    @Override
    public void damageImpl(DamageSource source, float amount, CallbackInfoReturnable<Boolean> cir) {
        Entity attacker = source.getAttacker();

        if (!(attacker instanceof PlayerEntity player))
            return;

        Optional<UUID> optional = Optional.ofNullable(this.getOwnerUuid());

        if (optional.isEmpty())
            return;

        if (DoormatSettings.disablePetAttacking.shouldBypassDamage(optional.get(), player))
            cir.setReturnValue(false);
    }

}
