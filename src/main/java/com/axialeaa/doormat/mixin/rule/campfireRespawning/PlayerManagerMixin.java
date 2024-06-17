package com.axialeaa.doormat.mixin.rule.campfireRespawning;

import com.axialeaa.doormat.DoormatSettings;
import com.axialeaa.doormat.helper.CampfireRespawningHelper;
import com.llamalad7.mixinextras.sugar.Local;
import com.llamalad7.mixinextras.sugar.Share;
import com.llamalad7.mixinextras.sugar.ref.LocalRef;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.entity.Entity;
import net.minecraft.registry.tag.BlockTags;
import net.minecraft.server.PlayerManager;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.math.BlockPos;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(PlayerManager.class)
public class PlayerManagerMixin {

    @Inject(method = "respawnPlayer", at = @At(value = "INVOKE", target = "Lnet/minecraft/server/network/ServerPlayNetworkHandler;requestTeleport(DDDFF)V"))
    private void shareArg(ServerPlayerEntity player, boolean alive, Entity.RemovalReason removalReason, CallbackInfoReturnable<ServerPlayerEntity> cir, @Share("playerArg") LocalRef<ServerPlayerEntity> playerRef, @Local(ordinal = 1) ServerPlayerEntity serverPlayerEntity) {
        playerRef.set(serverPlayerEntity);
    }

    @Inject(method = "respawnPlayer", at = @At(value = "INVOKE", target = "Lnet/minecraft/block/BlockState;isOf(Lnet/minecraft/block/Block;)Z", shift = At.Shift.BEFORE))
    private void playExtinguishSound(ServerPlayerEntity player, boolean alive, Entity.RemovalReason removalReason, CallbackInfoReturnable<ServerPlayerEntity> cir, @Local(ordinal = 1) ServerWorld serverWorld2, @Local BlockPos blockPos, @Local BlockState blockState, @Share("playerArg") LocalRef<ServerPlayerEntity> playerRef) {
        if (DoormatSettings.campfireRespawning && blockState.isIn(BlockTags.CAMPFIRES) && !blockState.isOf(Blocks.SOUL_CAMPFIRE))
            CampfireRespawningHelper.sendSoundPacket(serverWorld2, blockPos, playerRef.get());
    }

}
