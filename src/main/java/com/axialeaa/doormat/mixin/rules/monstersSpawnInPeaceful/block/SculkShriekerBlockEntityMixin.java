package com.axialeaa.doormat.mixin.rules.monstersSpawnInPeaceful.block;

import com.axialeaa.doormat.helpers.MonstersSpawnInPeaceful;
import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import net.minecraft.block.entity.SculkShriekerBlockEntity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.world.Difficulty;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

@Mixin(SculkShriekerBlockEntity.class)
public class SculkShriekerBlockEntityMixin {

    @WrapOperation(method = "canWarn", at = @At(value = "INVOKE", target = "Lnet/minecraft/server/world/ServerWorld;getDifficulty()Lnet/minecraft/world/Difficulty;"))
    private static Difficulty allowPeacefulSpawns(ServerWorld world, Operation<Difficulty> original) {
        return MonstersSpawnInPeaceful.bypassPeacefulCheck(world, original);
    }

}
