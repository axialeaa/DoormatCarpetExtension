package com.axialeaa.doormat.mixin.rule.disableEndTeleporting;

import com.axialeaa.doormat.DoormatSettings;
import com.llamalad7.mixinextras.injector.WrapWithCondition;
import net.minecraft.block.EndPortalBlock;
import net.minecraft.entity.Entity;
import net.minecraft.server.world.ServerWorld;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

@SuppressWarnings("unused")
@Mixin(EndPortalBlock.class)
public class EndPortalBlockMixin {

    @WrapWithCondition(method = "onEntityCollision", at = @At(value = "INVOKE", target = "Lnet/minecraft/entity/Entity;moveToWorld(Lnet/minecraft/server/world/ServerWorld;)Lnet/minecraft/entity/Entity;"))
    private boolean disableTeleportation(Entity entity, ServerWorld world) {
        return world.getDimension().bedWorks() || !DoormatSettings.disableEndTeleporting;
    }

}
