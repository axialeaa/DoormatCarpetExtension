package com.axialeaa.doormat.mixin.rule.lazyLoadedShearSuppression;

import com.axialeaa.doormat.settings.DoormatSettings;
import com.llamalad7.mixinextras.injector.v2.WrapWithCondition;
import net.minecraft.entity.Entity;
import net.minecraft.entity.passive.MooshroomEntity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

@Mixin(MooshroomEntity.class)
public class MooshroomEntityMixin {

    @WrapWithCondition(method = "sheared", at = @At(value = "INVOKE", target = "Lnet/minecraft/entity/passive/MooshroomEntity;discard()V"))
    private boolean shouldDiscardMooshroom(MooshroomEntity instance) {
        return !DoormatSettings.lazyLoadedShearSuppression || ((ServerWorld) instance.getEntityWorld()).shouldTickEntity(instance.getBlockPos());
    }

    @WrapWithCondition(method = "sheared", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/World;spawnEntity(Lnet/minecraft/entity/Entity;)Z", ordinal = 0))
    private boolean shouldSpawnCow(World instance, Entity entity) {
        return !DoormatSettings.lazyLoadedShearSuppression || ((ServerWorld) instance).shouldTickEntity(entity.getBlockPos());
    }

}
