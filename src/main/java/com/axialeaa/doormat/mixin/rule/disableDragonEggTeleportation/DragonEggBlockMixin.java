package com.axialeaa.doormat.mixin.rule.disableDragonEggTeleportation;

import com.axialeaa.doormat.DoormatSettings;
import com.llamalad7.mixinextras.injector.ModifyReturnValue;
import com.llamalad7.mixinextras.injector.v2.WrapWithCondition;
import net.minecraft.block.BlockState;
import net.minecraft.block.DragonEggBlock;
import net.minecraft.util.ActionResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

@Mixin(DragonEggBlock.class)
public class DragonEggBlockMixin {

    @WrapWithCondition(method = { "onUse", "onBlockBreakStart" }, at = @At(value = "INVOKE", target = "Lnet/minecraft/block/DragonEggBlock;teleport(Lnet/minecraft/block/BlockState;Lnet/minecraft/world/World;Lnet/minecraft/util/math/BlockPos;)V"))
    private boolean shouldTeleport(DragonEggBlock instance, BlockState state, World world, BlockPos pos) {
        return !DoormatSettings.disableDragonEggTeleportation;
    }

    /**
     * Disables the hand swinging animation when the dragon egg is clicked.
     */
    @ModifyReturnValue(method = "onUse", at = @At("RETURN"))
    private ActionResult disableHandSwing(ActionResult original) {
        return DoormatSettings.disableDragonEggTeleportation ? ActionResult.PASS : original;
    }

}
