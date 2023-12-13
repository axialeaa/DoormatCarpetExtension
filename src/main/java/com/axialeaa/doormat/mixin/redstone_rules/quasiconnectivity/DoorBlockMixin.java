package com.axialeaa.doormat.mixin.redstone_rules.quasiconnectivity;

import com.axialeaa.doormat.util.RedstoneRule;
import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import net.minecraft.block.DoorBlock;
import net.minecraft.block.enums.DoubleBlockHalf;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

@Mixin(DoorBlock.class)
public class DoorBlockMixin {

    /**
     * This is weird because doors are two blocks tall, and both halves need to activate at the same time.
     */
    @WrapOperation(method = "neighborUpdate", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/World;isReceivingRedstonePower(Lnet/minecraft/util/math/BlockPos;)Z", ordinal = 0))
    private boolean allowQuasiConnecting(World world, BlockPos pos, Operation<Boolean> original) {
        return original.call(world, pos) || RedstoneRule.qcValues.get(RedstoneRule.DOOR) && world.isReceivingRedstonePower(pos.up(world.getBlockState(pos).get(DoorBlock.HALF) == DoubleBlockHalf.LOWER ? 2 : 1));
    }

}
