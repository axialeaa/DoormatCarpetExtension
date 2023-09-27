package com.axialeaa.doormat.mixin.block_entity;

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

import java.util.Iterator;
import java.util.List;

@Mixin(BeaconBlockEntity.class)
public class BeaconBlockEntityMixin_PetHeal {

    @Inject(method = "applyPlayerEffects", at = @At(value = "RETURN"))
    private static void regenNearbyPets(World world, BlockPos pos, int beaconLevel, StatusEffect primaryEffect, StatusEffect secondaryEffect, CallbackInfo ci) {
        if (DoormatSettings.beaconsHealPets && secondaryEffect == StatusEffects.REGENERATION) {
            Box box = (new Box(pos)).expand(50).stretch(0.0, world.getHeight(), 0.0);
            List<TameableEntity> tameableEntityList = world.getEntitiesByClass(TameableEntity.class, box, TameableEntity::isTamed);
            Iterator<TameableEntity> tameableEntityIterator = tameableEntityList.iterator();

            TameableEntity tameableEntity;
            while (tameableEntityIterator.hasNext()) {
                tameableEntity = tameableEntityIterator.next();
                tameableEntity.addStatusEffect(new StatusEffectInstance(StatusEffects.REGENERATION, 340, 0, true, true));
            }

        }
    }

}
