package com.axialeaa.doormat.mixin.rule.disableNetherTeleporting;

import com.axialeaa.doormat.DoormatSettings;
import net.minecraft.block.BlockState;
import net.minecraft.block.NetherPortalBlock;
import net.minecraft.entity.Entity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(NetherPortalBlock.class)
public class NetherPortalBlockMixin {

    @Inject(method = "onEntityCollision", at = @At("HEAD"), cancellable = true)
    private void disableTeleportation(BlockState state, World world, BlockPos pos, Entity entity, CallbackInfo ci) {
        if (world.getDimension().bedWorks() && DoormatSettings.disableNetherTeleporting)
            ci.cancel();
    }

}
