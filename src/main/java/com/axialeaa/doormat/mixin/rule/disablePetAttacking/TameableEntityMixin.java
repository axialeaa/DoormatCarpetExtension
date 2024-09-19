package com.axialeaa.doormat.mixin.rule.disablePetAttacking;

import com.axialeaa.doormat.mixin.impl.EntityImplMixin;
import com.axialeaa.doormat.setting.DoormatSettings;
import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.entity.passive.TameableEntity;
import net.minecraft.entity.player.PlayerEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;

@Mixin(TameableEntity.class)
public abstract class TameableEntityMixin extends EntityImplMixin {

    @Shadow public abstract boolean isTamed();

    @Override
    public boolean isInvulnerableToImpl(DamageSource damageSource, Operation<Boolean> original) {
        if (!this.isTamed() || !(damageSource.getAttacker() instanceof PlayerEntity player))
            return original.call(damageSource);

        if (DoormatSettings.disablePetAttacking.shouldNegateDamage(player, TameableEntity.class.cast(this)))
            return true;

        return original.call(damageSource);
    }

}
