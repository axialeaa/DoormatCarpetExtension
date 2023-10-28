package com.axialeaa.doormat.mixin.rule.beaconsHealPets;

import com.axialeaa.doormat.DoormatSettings;
import com.llamalad7.mixinextras.sugar.Local;
import net.minecraft.block.entity.BeaconBlockEntity;
import net.minecraft.entity.effect.StatusEffect;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.entity.effect.StatusEffects;
import net.minecraft.entity.passive.TameableEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Box;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.List;

@Mixin(BeaconBlockEntity.class)
public class BeaconBlockEntityMixin {

    @Inject(method = "applyPlayerEffects", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/World;getNonSpectatingEntities(Ljava/lang/Class;Lnet/minecraft/util/math/Box;)Ljava/util/List;"))
    private static void regenNearbyPets(World world, BlockPos pos, int beaconLevel, StatusEffect primaryEffect, StatusEffect secondaryEffect, CallbackInfo ci, @Local Box box, @Local(ordinal = 2) int duration) {
        if (DoormatSettings.beaconsHealPets && secondaryEffect == StatusEffects.REGENERATION) {
            // if the rule is enabled and the beacon is set to regeneration...
            List<TameableEntity> list = world.getEntitiesByClass(TameableEntity.class, box, TameableEntity::isTamed);
            // make a list of all the tamed entities inside the effect box around the beacon
            for (TameableEntity tameableEntity : list) // give each of these entities regeneration
                tameableEntity.addStatusEffect(new StatusEffectInstance(StatusEffects.REGENERATION, duration, 0, true, true));
        }
    }

}
