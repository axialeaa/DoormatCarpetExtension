package com.axialeaa.doormat.mixin.rule.disablePrematureBlockFalling;

import com.axialeaa.doormat.setting.DoormatSettings;
import com.llamalad7.mixinextras.injector.v2.WrapWithCondition;
import net.minecraft.block.Block;
import net.minecraft.block.BrushableBlock;
import net.minecraft.block.FallingBlock;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.WorldAccess;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

@Mixin({
    FallingBlock.class,
    BrushableBlock.class
})
public abstract class Falling_BrushableBlockMixin {

    @WrapWithCondition(method = "getStateForNeighborUpdate", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/WorldAccess;scheduleBlockTick(Lnet/minecraft/util/math/BlockPos;Lnet/minecraft/block/Block;I)V"))
    private boolean shouldScheduleFall(WorldAccess instance, BlockPos pos, Block block, int delay) {
        return !DoormatSettings.disablePrematureBlockFalling || FallingBlock.canFallThrough(instance.getBlockState(pos.down()));
    }

}
