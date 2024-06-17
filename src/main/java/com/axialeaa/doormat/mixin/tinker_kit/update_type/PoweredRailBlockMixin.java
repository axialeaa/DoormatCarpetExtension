package com.axialeaa.doormat.mixin.tinker_kit.update_type;

import com.axialeaa.doormat.tinker_kit.TinkerKit;
import com.llamalad7.mixinextras.injector.v2.WrapWithCondition;
import com.llamalad7.mixinextras.sugar.Local;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.PoweredRailBlock;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyArg;

@Mixin(PoweredRailBlock.class)
public class PoweredRailBlockMixin {

    @ModifyArg(method = "updateBlockState", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/World;setBlockState(Lnet/minecraft/util/math/BlockPos;Lnet/minecraft/block/BlockState;I)Z"))
    private int changeUpdateType(int flags, @Local(argsOnly = true) BlockState state) {
        return TinkerKit.getFlags(state, flags);
    }

    @WrapWithCondition(method = "updateBlockState", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/World;updateNeighborsAlways(Lnet/minecraft/util/math/BlockPos;Lnet/minecraft/block/Block;)V"))
    private boolean shouldUpdateNeighbours(World instance, BlockPos pos, Block sourceBlock, @Local(argsOnly = true) BlockState state) {
        return TinkerKit.shouldUpdateNeighbours(state);
    }

}
