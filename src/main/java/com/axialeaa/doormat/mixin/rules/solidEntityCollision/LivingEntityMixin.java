package com.axialeaa.doormat.mixin.rules.solidEntityCollision;

import com.axialeaa.doormat.DoormatSettings;
import com.axialeaa.doormat.mixin.extensibility.EntityMixin;
import net.minecraft.entity.LivingEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(LivingEntity.class)
public abstract class LivingEntityMixin extends EntityMixin {

    @Shadow public abstract boolean isAlive();

    @Override
    public void isCollidableImpl(CallbackInfoReturnable<Boolean> cir) {
        if (DoormatSettings.solidEntityCollision && this.isAlive())
            cir.setReturnValue(true);
    }

}
