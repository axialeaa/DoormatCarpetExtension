package com.axialeaa.doormat.mixin.rule.maxMinecartSpeed;

import com.axialeaa.doormat.DoormatSettings;
import com.llamalad7.mixinextras.injector.ModifyReturnValue;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.vehicle.AbstractMinecartEntity;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

@Mixin(AbstractMinecartEntity.class)
public abstract class AbstractMinecartEntityMixin extends Entity {

    public AbstractMinecartEntityMixin(EntityType<?> type, World world) {
        super(type, world);
    }

    @ModifyReturnValue(method = "getMaxSpeed", at = @At("RETURN"))
    public double modifyMaxSpeedLand(double original) {
        return (isTouchingWater() ? DoormatSettings.maxMinecartSpeedWater : DoormatSettings.maxMinecartSpeedLand) / 20;
    }

}
