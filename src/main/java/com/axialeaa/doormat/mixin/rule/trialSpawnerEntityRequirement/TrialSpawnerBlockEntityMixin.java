package com.axialeaa.doormat.mixin.rule.trialSpawnerEntityRequirement;

import com.axialeaa.doormat.DoormatSettings;
import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import net.minecraft.block.entity.TrialSpawnerBlockEntity;
import net.minecraft.block.spawner.EntityDetector;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

@Mixin(TrialSpawnerBlockEntity.class)
public class TrialSpawnerBlockEntityMixin {

    @WrapOperation(method = "<init>", at = @At(value = "FIELD", target = "Lnet/minecraft/block/spawner/EntityDetector;SURVIVAL_PLAYERS:Lnet/minecraft/block/spawner/EntityDetector;"))
    private EntityDetector modifyDetector(Operation<EntityDetector> original) {
        return DoormatSettings.trialSpawnerEntityRequirement.getDetector();
    }

}