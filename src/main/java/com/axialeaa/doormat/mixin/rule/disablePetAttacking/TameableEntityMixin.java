package com.axialeaa.doormat.mixin.rule.disablePetAttacking;

import com.axialeaa.doormat.mixin.impl.EntityImplMixin;
import com.axialeaa.doormat.setting.DoormatSettings;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.entity.passive.TameableEntity;
import net.minecraft.entity.player.PlayerEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(TameableEntity.class)
public abstract class TameableEntityMixin extends EntityImplMixin {

    @Shadow public abstract boolean isTamed();

    @Override
    public void isInvulnerableToImpl(DamageSource damageSource, CallbackInfoReturnable<Boolean> cir) {
        if (!this.isTamed() || !(damageSource.getAttacker() instanceof PlayerEntity player))
            return;

        if (DoormatSettings.disablePetAttacking.shouldNegateDamage(player, TameableEntity.class.cast(this)))
            cir.setReturnValue(true);
    }

}
