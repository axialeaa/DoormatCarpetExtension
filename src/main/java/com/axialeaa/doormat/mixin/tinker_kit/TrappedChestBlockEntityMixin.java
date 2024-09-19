package com.axialeaa.doormat.mixin.tinker_kit;

import com.axialeaa.doormat.tinker_kit.TinkerKitUtils;
import com.llamalad7.mixinextras.injector.wrapmethod.WrapMethod;
import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.entity.TrappedChestBlockEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;

@Mixin(value = TrappedChestBlockEntity.class, priority = 1500)
public class TrappedChestBlockEntityMixin {

    @WrapMethod(method = "onViewerCountUpdate")
    private void cancelUpdates(World world, BlockPos pos, BlockState state, int oldViewerCount, int newViewerCount, Operation<Void> original) {
        Block block = state.getBlock();

        if (TinkerKitUtils.shouldUpdateNeighbours(block))
            original.call(world, pos, state, oldViewerCount, newViewerCount);
    }

}
