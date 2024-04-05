package com.axialeaa.doormat.mixin.rule.persistentRespawnAnchorCharge;

import com.axialeaa.doormat.DoormatSettings;
import com.llamalad7.mixinextras.injector.v2.WrapWithCondition;
import net.minecraft.network.packet.Packet;
import net.minecraft.server.PlayerManager;
import net.minecraft.server.network.ServerPlayNetworkHandler;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Slice;

@Mixin(PlayerManager.class)
public class PlayerManagerMixin {

    @WrapWithCondition(method = "respawnPlayer", slice = @Slice(from = @At(value = "INVOKE", target = "Lnet/minecraft/server/network/ServerPlayerEntity;setHealth(F)V")), at = @At(value = "INVOKE", target = "Lnet/minecraft/server/network/ServerPlayNetworkHandler;sendPacket(Lnet/minecraft/network/packet/Packet;)V", ordinal = 0))
    private boolean shouldPlaySound(ServerPlayNetworkHandler instance, Packet<?> packet) {
        return !DoormatSettings.persistentRespawnAnchorCharge;
    }

}
