package com.axialeaa.doormat.mixin.rule.disableMaceKnockback;

import com.axialeaa.doormat.DoormatSettings;
import com.llamalad7.mixinextras.injector.v2.WrapWithCondition;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.MaceItem;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

@Mixin(MaceItem.class)
public class MaceItemMixin {

    @WrapWithCondition(method = "postHit", at = @At(value = "INVOKE", target = "Lnet/minecraft/item/MaceItem;knockbackNearbyEntities(Lnet/minecraft/world/World;Lnet/minecraft/entity/player/PlayerEntity;Lnet/minecraft/entity/Entity;)V"))
    private boolean shouldKnockbackEntities(World world, PlayerEntity playerEntity, Entity entity) {
        return !DoormatSettings.disableMaceKnockback;
    }

}
