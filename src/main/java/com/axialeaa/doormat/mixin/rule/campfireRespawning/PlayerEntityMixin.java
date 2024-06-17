package com.axialeaa.doormat.mixin.rule.campfireRespawning;

import com.axialeaa.doormat.DoormatSettings;
import com.axialeaa.doormat.helper.CampfireRespawningHelper;
import com.llamalad7.mixinextras.sugar.Local;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.block.CampfireBlock;
import net.minecraft.entity.EntityType;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.WorldEvents;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.Optional;

@Mixin(ServerPlayerEntity.class)
public abstract class PlayerEntityMixin {

    @Inject(method = "findRespawnPosition", at = @At(value = "INVOKE", target = "Lnet/minecraft/block/BlockState;getBlock()Lnet/minecraft/block/Block;", ordinal = 0, shift = At.Shift.AFTER), cancellable = true)
    private static void respawnAtCampfire(ServerWorld world, BlockPos pos, float angle, boolean forced, boolean alive, CallbackInfoReturnable<Optional<Vec3d>> cir, @Local BlockState blockState) {
        if (DoormatSettings.campfireRespawning && CampfireBlock.isLitCampfire(blockState)) {
            Optional<Vec3d> optional = CampfireRespawningHelper.findRespawnPosition(EntityType.PLAYER, world, pos);

            if (!world.getBlockState(pos).isOf(Blocks.SOUL_CAMPFIRE) && !forced && !alive && optional.isPresent()) {
                CampfireBlock.extinguish(null, world, pos, blockState);

                world.setBlockState(pos, blockState.with(CampfireBlock.LIT, false));
                world.syncWorldEvent(null, WorldEvents.FIRE_EXTINGUISHED, pos, 0);
            }

            cir.setReturnValue(optional);
        }
    }

}
