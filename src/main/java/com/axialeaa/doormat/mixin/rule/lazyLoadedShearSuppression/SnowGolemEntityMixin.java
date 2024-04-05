package com.axialeaa.doormat.mixin.rule.lazyLoadedShearSuppression;

import com.axialeaa.doormat.DoormatSettings;
import com.llamalad7.mixinextras.injector.v2.WrapWithCondition;
import net.minecraft.entity.passive.SnowGolemEntity;
import net.minecraft.server.world.ServerWorld;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

@Mixin(SnowGolemEntity.class)
public class SnowGolemEntityMixin {

    @WrapWithCondition(method = "sheared", at = @At(value = "INVOKE", target = "Lnet/minecraft/entity/passive/SnowGolemEntity;setHasPumpkin(Z)V"))
    private boolean shouldRemovePumpkin(SnowGolemEntity instance, boolean hasPumpkin) {
        return !DoormatSettings.lazyLoadedShearSuppression || ((ServerWorld) instance.getEntityWorld()).shouldTickEntity(instance.getBlockPos());
    }

}
