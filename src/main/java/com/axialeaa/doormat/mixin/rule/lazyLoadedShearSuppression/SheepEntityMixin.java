package com.axialeaa.doormat.mixin.rule.lazyLoadedShearSuppression;

import com.axialeaa.doormat.settings.DoormatSettings;
import com.llamalad7.mixinextras.injector.v2.WrapWithCondition;
import net.minecraft.entity.passive.SheepEntity;
import net.minecraft.server.world.ServerWorld;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

@Mixin(SheepEntity.class)
public class SheepEntityMixin {

    @WrapWithCondition(method = "sheared", at = @At(value = "INVOKE", target = "Lnet/minecraft/entity/passive/SheepEntity;setSheared(Z)V"))
    private boolean shouldSetSheared(SheepEntity instance, boolean sheared) {
        return !DoormatSettings.lazyLoadedShearSuppression || ((ServerWorld) instance.getEntityWorld()).shouldTickEntity(instance.getBlockPos());
    }

}
