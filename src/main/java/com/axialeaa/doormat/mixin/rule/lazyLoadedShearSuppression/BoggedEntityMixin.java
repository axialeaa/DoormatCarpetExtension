package com.axialeaa.doormat.mixin.rule.lazyLoadedShearSuppression;

import com.axialeaa.doormat.settings.DoormatSettings;
import com.llamalad7.mixinextras.injector.v2.WrapWithCondition;
import net.minecraft.entity.mob.BoggedEntity;
import net.minecraft.server.world.ServerWorld;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

@Mixin(BoggedEntity.class)
public class BoggedEntityMixin {

    @WrapWithCondition(method = "sheared", at = @At(value = "INVOKE", target = "Lnet/minecraft/entity/mob/BoggedEntity;setSheared(Z)V"))
    private boolean shouldSetSheared(BoggedEntity instance, boolean sheared) {
        return !DoormatSettings.lazyLoadedShearSuppression || ((ServerWorld) instance.getEntityWorld()).shouldTickEntity(instance.getBlockPos());
    }

}
