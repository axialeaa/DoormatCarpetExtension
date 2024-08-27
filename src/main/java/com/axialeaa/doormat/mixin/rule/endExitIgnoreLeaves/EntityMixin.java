package com.axialeaa.doormat.mixin.rule.endExitIgnoreLeaves;

import com.axialeaa.doormat.settings.DoormatSettings;
import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import net.minecraft.entity.Entity;
import net.minecraft.world.Heightmap;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

@Mixin(Entity.class)
public class EntityMixin {

    @WrapOperation(method = "getWorldSpawnPos", at = @At(value = "FIELD", target = "Lnet/minecraft/world/Heightmap$Type;MOTION_BLOCKING_NO_LEAVES:Lnet/minecraft/world/Heightmap$Type;"))
    private Heightmap.Type setEndExitHeightmap(Operation<Heightmap.Type> original) {
        return DoormatSettings.endExitIgnoreLeaves ? original.call() : Heightmap.Type.MOTION_BLOCKING;
    }

}
