package com.axialeaa.doormat.mixin.rule.alwaysHalloween;

import com.axialeaa.doormat.DoormatSettings;
import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import net.minecraft.entity.mob.AbstractSkeletonEntity;
import net.minecraft.entity.mob.ZombieEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

import java.time.LocalDate;
import java.time.temporal.TemporalField;

@Mixin({ AbstractSkeletonEntity.class, ZombieEntity.class })
public class AbstractSkeleton_ZombieEntityMixin {

    @WrapOperation(method = "initialize", at = @At(value = "INVOKE", target = "Ljava/time/LocalDate;get(Ljava/time/temporal/TemporalField;)I", ordinal = 0))
    private int modifyDayCall(LocalDate instance, TemporalField field, Operation<Integer> original) {
        return DoormatSettings.alwaysHalloween ? 31 : original.call(instance, field);
    }

    @WrapOperation(method = "initialize", at = @At(value = "INVOKE", target = "Ljava/time/LocalDate;get(Ljava/time/temporal/TemporalField;)I", ordinal = 1))
    private int modifyMonthCall(LocalDate instance, TemporalField field, Operation<Integer> original) {
        return DoormatSettings.alwaysHalloween ? 10 : original.call(instance, field);
    }

}
