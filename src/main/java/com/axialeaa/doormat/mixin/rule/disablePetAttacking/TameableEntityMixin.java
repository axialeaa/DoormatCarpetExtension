package com.axialeaa.doormat.mixin.rule.disablePetAttacking;

import com.axialeaa.doormat.DoormatSettings;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.entity.passive.AnimalEntity;
import net.minecraft.entity.passive.TameableEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.world.World;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;

import java.util.Optional;
import java.util.UUID;

@Mixin(TameableEntity.class)
public abstract class TameableEntityMixin extends AnimalEntity {

    protected TameableEntityMixin(EntityType<? extends AnimalEntity> entityType, World world) {
        super(entityType, world);
    }

    @Shadow @Nullable public abstract UUID getOwnerUuid();

    @Unique
    public boolean damage(DamageSource source, float amount) {
        Entity attacker = source.getAttacker();
        if (DoormatSettings.disablePetAttacking.isEnabled() && attacker instanceof PlayerEntity playerEntity) {
            // if disablePetAttacking is "true" or "if_owner" and the attacker in question is a player, get the uuid of the owner of the mob
            Optional<UUID> optional = Optional.ofNullable(this.getOwnerUuid());
            if ((DoormatSettings.disablePetAttacking == DoormatSettings.PetHurtMode.TRUE) || (DoormatSettings.disablePetAttacking == DoormatSettings.PetHurtMode.IF_OWNER && optional.isPresent() && playerEntity.getUuid().equals(optional.get()))) {
                //if the owner uuid exists (optional.isPresent()), that means the mob is tamed
                // if disablePetAttacking is either "true", or "if_owner" and the uuid matches the owner of the pet, return false
                return false;
            }
        }
        return super.damage(source, amount);
    }

}
