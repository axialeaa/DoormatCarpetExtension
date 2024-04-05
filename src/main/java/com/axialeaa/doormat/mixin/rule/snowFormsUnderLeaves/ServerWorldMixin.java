package com.axialeaa.doormat.mixin.rule.snowFormsUnderLeaves;

import com.axialeaa.doormat.DoormatSettings;
import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.world.Heightmap;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

@Mixin(ServerWorld.class)
public class ServerWorldMixin {

    @WrapOperation(method = "tickIceAndSnow", at = @At(value = "FIELD", target = "Lnet/minecraft/world/Heightmap$Type;MOTION_BLOCKING:Lnet/minecraft/world/Heightmap$Type;"))
    private Heightmap.Type modifyGenerationHeightmap(Operation<Heightmap.Type> original) {
        return DoormatSettings.snowFormsUnderLeaves ? Heightmap.Type.MOTION_BLOCKING_NO_LEAVES : original.call();
    }

}
