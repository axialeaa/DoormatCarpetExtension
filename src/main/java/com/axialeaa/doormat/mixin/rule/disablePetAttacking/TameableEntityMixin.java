package com.axialeaa.doormat.mixin.rule.disablePetAttacking;

import com.axialeaa.doormat.mixin.impl.EntityImplMixin;
import com.axialeaa.doormat.setting.DoormatSettings;
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
    @Shadow public abstract boolean isTamed();

    @Override
    public void damageImpl(DamageSource source, float amount, CallbackInfoReturnable<Boolean> cir) {
        if (!this.isTamed() || !(source.getAttacker() instanceof PlayerEntity player))
            return;

        Optional<UUID> optional = Optional.ofNullable(this.getOwnerUuid());

        if (optional.isEmpty())
            return;

        if (DoormatSettings.disablePetAttacking.shouldNegateDamage(player, TameableEntity.class.cast(this)))
            cir.setReturnValue(false);
    }

}
