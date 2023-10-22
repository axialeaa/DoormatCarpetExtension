package com.axialeaa.doormat.mixin.rule.disableEnd;

import com.axialeaa.doormat.DoormatSettings;
import net.minecraft.block.BlockState;
import net.minecraft.block.EndPortalBlock;
import net.minecraft.entity.Entity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(EndPortalBlock.class)
public class EndPortalBlockMixin {

    @Inject(method = "onEntityCollision", at = @At("HEAD"), cancellable = true)
    private void disableTeleportation(BlockState state, World world, BlockPos pos, Entity entity, CallbackInfo ci) {
        if (world.getDimension().bedWorks() && DoormatSettings.disableEnd)
            ci.cancel();
    }

}
