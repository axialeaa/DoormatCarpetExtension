package com.axialeaa.doormat.mixin.rules.reachAroundBridging;

import com.axialeaa.doormat.DoormatSettings;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.client.world.ClientWorld;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.hit.HitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.util.math.Vec3d;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Environment(EnvType.CLIENT)
@Mixin(MinecraftClient.class)
public class MinecraftClientMixin {

    @Shadow @Nullable public ClientPlayerEntity player;
    @Shadow @Nullable public HitResult crosshairTarget;
    @Shadow @Nullable public ClientWorld world;

    /**
     * If the rule is enabled, allows players to place blocks one block in front of the one they're standing on.
     * @implNote This works by setting the crosshair target to the blockpos of the standing position, offset by 1 in the facing direction.
     */
    @Inject(method = "doItemUse", at = @At(value = "INVOKE", target = "Lnet/minecraft/util/hit/HitResult;getType()Lnet/minecraft/util/hit/HitResult$Type;", shift = At.Shift.BEFORE))
    private void setCrosshairTargetForward(CallbackInfo ci) {
        if (DoormatSettings.reachAroundBridging && player != null && world != null) {
            Direction direction = player.getHorizontalFacing();
            BlockPos blockPos = player.getSteppingPos().offset(direction);
            boolean canPlaceBlock = player.isOnGround() && player.getPitch() > 25;
            if (this.crosshairTarget.getType() == HitResult.Type.MISS && canPlaceBlock && world.getBlockState(blockPos).isReplaceable())
                this.crosshairTarget = new BlockHitResult(Vec3d.ofCenter(blockPos), direction, blockPos, false);
        }
    }

}