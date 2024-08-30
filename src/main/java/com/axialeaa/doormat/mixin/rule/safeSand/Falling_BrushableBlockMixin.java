package com.axialeaa.doormat.mixin.rule.safeSand;

import com.axialeaa.doormat.setting.DoormatSettings;
import com.llamalad7.mixinextras.injector.v2.WrapWithCondition;
import com.llamalad7.mixinextras.sugar.Local;
import net.minecraft.block.Block;
import net.minecraft.block.BrushableBlock;
import net.minecraft.block.FallingBlock;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.world.WorldAccess;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

@Mixin({
    FallingBlock.class,
    BrushableBlock.class
})
public class Falling_BrushableBlockMixin {

    @WrapWithCondition(method = "getStateForNeighborUpdate", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/WorldAccess;scheduleBlockTick(Lnet/minecraft/util/math/BlockPos;Lnet/minecraft/block/Block;I)V"))
    private boolean shouldScheduleFall(WorldAccess instance, BlockPos pos, Block block, int delay, @Local(argsOnly = true) Direction direction) {
        return !DoormatSettings.safeSand || direction == Direction.DOWN;
    }

}
