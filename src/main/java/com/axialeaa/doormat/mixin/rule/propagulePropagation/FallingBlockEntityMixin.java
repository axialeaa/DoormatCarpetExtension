package com.axialeaa.doormat.mixin.rule.propagulePropagation;

import com.axialeaa.doormat.setting.DoormatSettings;
import com.llamalad7.mixinextras.sugar.Local;
import net.minecraft.block.*;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.FallingBlockEntity;
import net.minecraft.network.packet.s2c.play.BlockUpdateS2CPacket;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(FallingBlockEntity.class)
public abstract class FallingBlockEntityMixin extends Entity {

    @Shadow private BlockState block;

    public FallingBlockEntityMixin(EntityType<?> type, World world) {
        super(type, world);
    }

    @Inject(method = "tick", at = @At(value = "INVOKE", target = "Lnet/minecraft/entity/FallingBlockEntity;discard()V", ordinal = 3, shift = At.Shift.BEFORE), cancellable = true)
    private void placeStandingVariant(CallbackInfo ci, @Local BlockPos blockPos) {
        if (!DoormatSettings.propagulePropagation || !this.block.getOrEmpty(PropaguleBlock.HANGING).orElse(true))
            return;

        World world = this.getWorld();

        BlockState standingState = this.block.with(PropaguleBlock.HANGING, false);
        BlockState downState = world.getBlockState(blockPos.down());

        if (standingState.canPlaceAt(world, blockPos) && !FallingBlock.canFallThrough(downState) && world.setBlockState(blockPos, standingState, Block.NOTIFY_ALL)) {
            ((ServerWorld) world).getChunkManager().chunkLoadingManager.sendToOtherNearbyPlayers(this, new BlockUpdateS2CPacket(blockPos, standingState));
            this.discard();

            ci.cancel();
        }
    }

}