package com.axialeaa.doormat.mixin.rule.solidEntityCollision;

import com.axialeaa.doormat.setting.DoormatSettings;
import com.axialeaa.doormat.mixin.impl.EntityImplMixin;
import net.minecraft.entity.LivingEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(LivingEntity.class)
public abstract class LivingEntityMixin extends EntityImplMixin {

    @Shadow public abstract boolean isAlive();

    @Override
    public void isCollidableImpl(CallbackInfoReturnable<Boolean> cir) {
        if (DoormatSettings.solidEntityCollision && this.isAlive())
            cir.setReturnValue(true);
    }

}
