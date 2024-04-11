package com.axialeaa.doormat.mixin.tinker_kit.quasiconnectivity;

import com.axialeaa.doormat.tinker_kit.TinkerKit;
import com.llamalad7.mixinextras.injector.ModifyReturnValue;
import net.minecraft.block.AbstractRedstoneGateBlock;
import net.minecraft.block.BlockState;
import net.minecraft.block.HorizontalFacingBlock;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

@Mixin(AbstractRedstoneGateBlock.class)
public class AbstractRedstoneGateBlockMixin {

    @ModifyReturnValue(method = "getPower", at = @At(value = "RETURN", ordinal = 1))
    private int getQuasiPower(int original, World world, BlockPos pos, BlockState state) {
        return TinkerKit.getEmittedRedstonePower(world, pos, state.get(HorizontalFacingBlock.FACING));
    }

}
