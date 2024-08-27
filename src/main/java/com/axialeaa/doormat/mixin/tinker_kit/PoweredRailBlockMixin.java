package com.axialeaa.doormat.mixin.tinker_kit;

import com.axialeaa.doormat.mixin.impl.AbstractBlockImplMixin;
import com.axialeaa.doormat.tinker_kit.TinkerKit;
import com.llamalad7.mixinextras.injector.v2.WrapWithCondition;
import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import com.llamalad7.mixinextras.sugar.Local;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.PoweredRailBlock;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyArg;

@Mixin(value = PoweredRailBlock.class, priority = 1500)
public class PoweredRailBlockMixin extends AbstractBlockImplMixin {

    @WrapOperation(method = "isPoweredByOtherRails(Lnet/minecraft/world/World;Lnet/minecraft/util/math/BlockPos;ZILnet/minecraft/block/enums/RailShape;)Z", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/World;isReceivingRedstonePower(Lnet/minecraft/util/math/BlockPos;)Z"))
    private boolean allowQuasiConnectivity_isPoweredByOtherRails(World instance, BlockPos pos, Operation<Boolean> original, @Local BlockState state) {
        Block block = state.getBlock();
        return TinkerKit.isReceivingRedstonePower(instance, pos, block);
    }

    @WrapOperation(method = "updateBlockState", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/World;isReceivingRedstonePower(Lnet/minecraft/util/math/BlockPos;)Z"))
    private boolean allowQuasiConnectivity_updateBlockState(World instance, BlockPos pos, Operation<Boolean> original, @Local(argsOnly = true) BlockState state) {
        Block block = state.getBlock();
        return TinkerKit.isReceivingRedstonePower(instance, pos, block);
    }

    @ModifyArg(method = "updateBlockState", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/World;setBlockState(Lnet/minecraft/util/math/BlockPos;Lnet/minecraft/block/BlockState;I)Z"))
    private int modifyUpdateType(int original, @Local(argsOnly = true) BlockState state) {
        Block block = state.getBlock();
        return TinkerKit.getFlags(block, original);
    }

    @WrapWithCondition(method = "updateBlockState", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/World;updateNeighborsAlways(Lnet/minecraft/util/math/BlockPos;Lnet/minecraft/block/Block;)V"))
    private boolean shouldUpdateNeighbours(World instance, BlockPos pos, Block sourceBlock, @Local(argsOnly = true) BlockState state) {
        Block block = state.getBlock();
        return TinkerKit.shouldUpdateNeighbours(block);
    }

}
