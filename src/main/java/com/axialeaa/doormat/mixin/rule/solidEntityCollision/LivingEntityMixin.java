package com.axialeaa.doormat.mixin.rule.solidEntityCollision;

import com.axialeaa.doormat.mixin.impl.EntityImplMixin;
import com.axialeaa.doormat.setting.DoormatSettings;
import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import net.minecraft.entity.LivingEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;

@Mixin(LivingEntity.class)
public abstract class LivingEntityMixin extends EntityImplMixin {

    @Shadow public abstract boolean isAlive();

    @Override
    public boolean isCollidableImpl(Operation<Boolean> original) {
        if (DoormatSettings.solidEntityCollision && this.isAlive())
            return true;

        return original.call();
    }

}
