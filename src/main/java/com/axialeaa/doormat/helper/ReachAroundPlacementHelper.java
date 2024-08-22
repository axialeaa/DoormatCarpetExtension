package com.axialeaa.doormat.helper;

import com.axialeaa.doormat.DoormatSettings;
import net.minecraft.block.BlockState;
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
        if (!DoormatSettings.reachAroundBridging)
            return crosshairTarget;

        Direction direction = player.getHorizontalFacing();
        BlockPos blockPos = player.getSteppingPos().offset(direction);

        BlockState blockState = world.getBlockState(blockPos);

        if (crosshairTarget.getType() != HitResult.Type.MISS || !player.isOnGround() || !blockState.isReplaceable())
            return crosshairTarget;

        Vec3d startPos = player.getEyePos();
        Vec3d endPos = player.getRotationVec(1.0F).multiply(player.getBlockInteractionRange()).add(startPos);

        Optional<Vec3d> optional = new Box(blockPos).raycast(startPos, endPos);

        if (optional.isEmpty())
            return crosshairTarget;

        return new BlockHitResult(Vec3d.ofCenter(blockPos), direction, blockPos, false);
    }

}
