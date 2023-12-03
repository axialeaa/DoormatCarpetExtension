package com.axialeaa.doormat.mixin.rules.reachAroundBridging;

import com.axialeaa.doormat.DoormatSettings;
import com.llamalad7.mixinextras.sugar.Local;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.client.network.ClientPlayerInteractionManager;
import net.minecraft.client.world.ClientWorld;
import net.minecraft.util.Hand;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.hit.HitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Box;
import net.minecraft.util.math.Direction;
import net.minecraft.util.math.Vec3d;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.Objects;
import java.util.Optional;

@Environment(EnvType.CLIENT)
@Mixin(MinecraftClient.class)
public abstract class MinecraftClientMixin {

    @Shadow @Nullable public ClientPlayerEntity player;
    @Shadow @Nullable public HitResult crosshairTarget;
    @Shadow @Nullable public ClientWorld world;
    @Shadow @Nullable public ClientPlayerInteractionManager interactionManager;

    /**
     * If the rule is enabled, allows players to place blocks one block in front of the one they're standing on.
     * @implNote This works by checking if the line between the player and the end of their reach distance intersects with the blockpos of the standing position, offset by 1 in the facing direction. Thanks again for the help, <a href="https://github.com/lntricate1">intricate</a>!
     */
    @Inject(method = "doItemUse", at = @At(value = "INVOKE", target = "Lnet/minecraft/util/hit/HitResult;getType()Lnet/minecraft/util/hit/HitResult$Type;", shift = At.Shift.BEFORE))
    private void setCrosshairTargetForward(CallbackInfo ci, @Local Hand hand) {
        if (DoormatSettings.reachAroundBridging && player != null && world != null) {
            Direction direction = player.getHorizontalFacing();
            BlockPos blockPos = player.getSteppingPos().offset(direction);

            if (crosshairTarget.getType() == HitResult.Type.MISS && player.isOnGround() && world.getBlockState(blockPos).isReplaceable()) {
                Vec3d startPos = player.getEyePos();
                Vec3d endPos = player.getRotationVec(1.0F).multiply(Objects.requireNonNull(interactionManager).getReachDistance()).add(startPos);
                Optional<Vec3d> optional = new Box(blockPos).raycast(startPos, endPos);
                if (optional.isPresent())
                    crosshairTarget = new BlockHitResult(Vec3d.ofCenter(blockPos), direction, blockPos, false);
            }
        }
    }

}