package com.axialeaa.doormat.mixin.rules.peacefulMonsterSpawning.entity;

import com.axialeaa.doormat.helpers.PeacefulMonsterSpawning;
import com.llamalad7.mixinextras.injector.ModifyExpressionValue;
import net.minecraft.entity.passive.PigEntity;
import net.minecraft.entity.passive.VillagerEntity;
import net.minecraft.world.Difficulty;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

@Mixin({PigEntity.class, VillagerEntity.class})
public class Pig_VillagerEntityMixin {

    /**
     * Enables lightning conversions in peaceful mode, instead of setting the mob on fire.
     */
    @ModifyExpressionValue(method = "onStruckByLightning", at = @At(value = "INVOKE", target = "Lnet/minecraft/server/world/ServerWorld;getDifficulty()Lnet/minecraft/world/Difficulty;"))
    private static Difficulty bypassPeacefulConversionCheck(Difficulty original) {
        return PeacefulMonsterSpawning.bypassPeacefulCheck(original);
    }

}
