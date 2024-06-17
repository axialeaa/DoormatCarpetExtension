package com.axialeaa.doormat.helper;

import com.axialeaa.doormat.DoormatSettings;
import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.client.world.ClientWorld;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.hit.HitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Box;
import net.minecraft.util.math.Direction;
import net.minecraft.util.math.Vec3d;

import java.util.Optional;

public class ReachAroundPlacementHelper {

    public static HitResult getHitResult(ClientWorld world, ClientPlayerEntity player, HitResult crosshairTarget) {
        HitResult hitResult = crosshairTarget;

        if (DoormatSettings.reachAroundBridging) {
            Direction direction = player.getHorizontalFacing();
            BlockPos blockPos = player.getSteppingPos().offset(direction);

            if (crosshairTarget != null && crosshairTarget.getType() == HitResult.Type.MISS && player.isOnGround() && world.getBlockState(blockPos).isReplaceable()) {
                Vec3d startPos = player.getEyePos();
                Vec3d endPos = player.getRotationVec(1.0F).multiply(player.getBlockInteractionRange()).add(startPos);
                Optional<Vec3d> optional = new Box(blockPos).raycast(startPos, endPos);

//                Color color = RenderHandler.getTrubetskoyColor("white");

                if (optional.isPresent()) {
//                    color = RenderHandler.getTrubetskoyColor("green");
                    hitResult = new BlockHitResult(Vec3d.ofCenter(blockPos), direction, blockPos, false);
                }

//                if (DoormatServer.IS_DEBUG)
//                    renderDebug(blockPos, startPos, endPos, optional.orElse(null), color);
            }
        }

        return hitResult;
    }

//    private static void renderDebug(BlockPos pos, Vec3d vecStart, Vec3d vecEnd, @Nullable Vec3d intersection, Color color) {
//        // Enclosing box
//        RenderHandler.addCuboidLines(Vec3d.ofCenter(pos), 0.5, RenderHandler.getTrubetskoyColor("white"), 160, true);
//        // Facing vector
//        RenderHandler.addLine(vecStart.getX(), vecStart.getY(), vecStart.getZ(), vecEnd.getX(), vecEnd.getY(), vecEnd.getZ(), color, 160, false);
//
//        // Intersection point
//        if (intersection != null)
//            RenderHandler.addCuboidQuads(intersection, 0.02, color, 160, false);
//    }

}
