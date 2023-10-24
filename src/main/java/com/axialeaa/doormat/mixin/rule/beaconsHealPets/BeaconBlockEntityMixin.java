package com.axialeaa.doormat.mixin.rule.beaconsHealPets;

import com.axialeaa.doormat.DoormatSettings;
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

    @Inject(method = "applyPlayerEffects", at = @At(value = "HEAD"))
    private static void regenNearbyPets(World world, BlockPos pos, int beaconLevel, StatusEffect primaryEffect, StatusEffect secondaryEffect, CallbackInfo ci) {
        if (DoormatSettings.beaconsHealPets && secondaryEffect == StatusEffects.REGENERATION) {
            // if the rule is enabled and the beacon is set to regeneration...
            Box box = (new Box(pos)).expand(50).stretch(0.0, world.getHeight(), 0.0);
            // create a box around the beacon, and make a list of all the tamed entities inside
            List<TameableEntity> list = world.getEntitiesByClass(TameableEntity.class, box, TameableEntity::isTamed);
            for (TameableEntity tameableEntity : list) // give each of these entities regeneration for 340 ticks (17 seconds)
                tameableEntity.addStatusEffect(new StatusEffectInstance(StatusEffects.REGENERATION, 340, 0, true, true));
        }
    }

}
