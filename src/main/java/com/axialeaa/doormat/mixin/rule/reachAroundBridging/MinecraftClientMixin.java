package com.axialeaa.doormat.mixin.rule.reachAroundBridging;

import com.axialeaa.doormat.DoormatServer;
import com.axialeaa.doormat.DoormatSettings;
import com.axialeaa.doormat.util.RenderHandler;
import com.llamalad7.mixinextras.sugar.Local;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.network.ClientPlayerEntity;
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

import java.util.Optional;

@Environment(EnvType.CLIENT)
@Mixin(MinecraftClient.class)
public abstract class MinecraftClientMixin {

    @Shadow @Nullable public ClientPlayerEntity player;
    @Shadow @Nullable public HitResult crosshairTarget;
    @Shadow @Nullable public ClientWorld world;

    /**
     * If the rule is enabled, allows players to place blocks one block in front of the one they're standing on.
     * @implNote This works by checking if the line between the player and the end of their reach distance intersects with the blockpos of the standing position, offset by 1 in the facing direction. Thanks again for the help, <a href="https://github.com/lntricate1">intricate</a>!
     */
    @Inject(method = "doItemUse", at = @At(value = "INVOKE", target = "Lnet/minecraft/util/hit/HitResult;getType()Lnet/minecraft/util/hit/HitResult$Type;", shift = At.Shift.BEFORE))
    private void setCrosshairTargetForward(CallbackInfo ci, @Local Hand hand) {
        if (DoormatSettings.reachAroundBridging && player != null && world != null) {
            Direction direction = player.getHorizontalFacing();
            BlockPos blockPos = player.getSteppingPos().offset(direction);

            if (crosshairTarget != null && crosshairTarget.getType() == HitResult.Type.MISS && player.isOnGround() && world.getBlockState(blockPos).isReplaceable()) {
                Vec3d startPos = player.getEyePos();
                Vec3d endPos = player.getRotationVec(1.0F).multiply(player.getBlockInteractionRange()).add(startPos);
                Optional<Vec3d> optional = new Box(blockPos).raycast(startPos, endPos);
                int strokeColor = 0xFFFFFFFF;

                if (optional.isPresent()) {
                    strokeColor = 0xFF30FF30;
                    crosshairTarget = new BlockHitResult(Vec3d.ofCenter(blockPos), direction, blockPos, false);
                }
                if (DoormatServer.IS_DEBUG) {
                    RenderHandler.addCuboidWireFrame(Vec3d.ofCenter(blockPos), 0.5, 0xFFFFFFFF, 8000);
                    RenderHandler.addLine(startPos.getX(), startPos.getY(), startPos.getZ(), endPos.getX(), endPos.getY(), endPos.getZ(), strokeColor, 8000);
                    if (optional.isPresent())
                        RenderHandler.addCuboidFilled(optional.get(), 0.02, strokeColor, 0x00000000, 8000);
                }
            }
        }
    }

}