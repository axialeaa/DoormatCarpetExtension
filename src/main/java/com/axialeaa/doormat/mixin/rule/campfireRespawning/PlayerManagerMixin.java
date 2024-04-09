package com.axialeaa.doormat.mixin.rule.campfireRespawning;

import com.axialeaa.doormat.DoormatSettings;
import com.axialeaa.doormat.helper.CampfireRespawningHelper;
import com.llamalad7.mixinextras.injector.ModifyExpressionValue;
import com.llamalad7.mixinextras.sugar.Local;
import net.minecraft.block.BlockState;
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

    @ModifyExpressionValue(method = "respawnPlayer", at = @At(value = "INVOKE", target = "Lnet/minecraft/block/BlockState;isIn(Lnet/minecraft/registry/tag/TagKey;)Z"))
    private boolean fixAngle(boolean original, @Local BlockState blockState) {
        return original || blockState.isIn(BlockTags.CAMPFIRES) && !DoormatSettings.campfireRespawning;
    }

    @Inject(method = "respawnPlayer", at = @At(value = "RETURN", shift = At.Shift.BEFORE))
    private void playExtinguishSound(ServerPlayerEntity player, boolean alive, CallbackInfoReturnable<ServerPlayerEntity> cir, @Local BlockPos blockPos, @Local(ordinal = 1) ServerWorld serverWorld2) {
        CampfireRespawningHelper.sendSoundPacket(serverWorld2, blockPos, player);
    }

}
