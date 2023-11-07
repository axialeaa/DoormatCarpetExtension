package com.axialeaa.doormat.mixin.rules.disablePetAttacking;

import com.axialeaa.doormat.DoormatSettings;
import com.axialeaa.doormat.mixin.extendables.LivingEntityMixin;
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
public abstract class TameableEntityMixin extends LivingEntityMixin {

    @Shadow @Nullable public abstract UUID getOwnerUuid();

    @Override
    public void injectedDamage(DamageSource source, float amount, CallbackInfoReturnable<Boolean> cir) {
        Entity attacker = source.getAttacker();
        if (attacker instanceof PlayerEntity playerEntity) {
            Optional<UUID> optional = Optional.ofNullable(this.getOwnerUuid());
            // if the attacker is a player, get the uuid of the owner of the mob
            if (DoormatSettings.disablePetAttacking == DoormatSettings.PetAttackMode.TRUE ||
                (DoormatSettings.disablePetAttacking == DoormatSettings.PetAttackMode.OWNED && optional.isPresent() && playerEntity.getUuid().equals(optional.get())))
                // if disablePetAttacking is "true" or "owned" and the attacking player is the owner, return false
                // if the owner uuid exists (optional.isPresent()), that means the mob is tamed
                cir.setReturnValue(false);
        }
    }

}
