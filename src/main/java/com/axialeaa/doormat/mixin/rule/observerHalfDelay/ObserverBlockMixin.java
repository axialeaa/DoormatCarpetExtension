package com.axialeaa.doormat.mixin.rule.observerHalfDelay;

import com.axialeaa.doormat.settings.DoormatSettings;
import com.llamalad7.mixinextras.sugar.Local;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.block.ObserverBlock;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.WorldAccess;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyArg;

@Mixin(ObserverBlock.class)
public class ObserverBlockMixin {

    @ModifyArg(method = "scheduleTick", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/WorldAccess;scheduleBlockTick(Lnet/minecraft/util/math/BlockPos;Lnet/minecraft/block/Block;I)V"))
    private int halveDelay(int original, @Local(argsOnly = true) WorldAccess world, @Local(argsOnly = true) BlockPos pos) {
        BlockState blockState = world.getBlockState(pos.down());

        if (original <= 1)
            return original;

        return DoormatSettings.observerHalfDelay && blockState.isOf(Blocks.REDSTONE_ORE) ? original / 2 : original;
    }

}
