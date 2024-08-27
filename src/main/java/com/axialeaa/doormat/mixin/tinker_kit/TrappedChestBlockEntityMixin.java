package com.axialeaa.doormat.mixin.tinker_kit;

import com.axialeaa.doormat.tinker_kit.TinkerKit;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.entity.TrappedChestBlockEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(value = TrappedChestBlockEntity.class, priority = 1500)
public class TrappedChestBlockEntityMixin {

    @Inject(method = "onViewerCountUpdate", at = @At(value = "INVOKE", target = "Lnet/minecraft/block/entity/ChestBlockEntity;onViewerCountUpdate(Lnet/minecraft/world/World;Lnet/minecraft/util/math/BlockPos;Lnet/minecraft/block/BlockState;II)V", shift = At.Shift.AFTER), cancellable = true)
    private void cancelUpdates(World world, BlockPos pos, BlockState state, int oldViewerCount, int newViewerCount, CallbackInfo ci) {
        Block block = state.getBlock();

        if (!TinkerKit.shouldUpdateNeighbours(block))
            ci.cancel();
    }

}
