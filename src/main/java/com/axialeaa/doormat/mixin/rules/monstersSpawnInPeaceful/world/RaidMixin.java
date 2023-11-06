package com.axialeaa.doormat.mixin.rules.monstersSpawnInPeaceful.world;

import com.axialeaa.doormat.helpers.MonstersSpawnInPeaceful;
import com.llamalad7.mixinextras.injector.ModifyExpressionValue;
import net.minecraft.village.raid.Raid;
import net.minecraft.world.Difficulty;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

@Mixin(Raid.class)
public class RaidMixin {

    @ModifyExpressionValue(method = "tick", at = @At(value = "INVOKE", target = "Lnet/minecraft/server/world/ServerWorld;getDifficulty()Lnet/minecraft/world/Difficulty;"))
    private static Difficulty bypassPeacefulInvalidationCheck(Difficulty original) {
        return MonstersSpawnInPeaceful.bypassPeacefulCheck(original);
    }

}
