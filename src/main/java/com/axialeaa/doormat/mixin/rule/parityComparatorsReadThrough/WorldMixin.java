package com.axialeaa.doormat.mixin.rule.parityComparatorsReadThrough;

import com.axialeaa.doormat.DoormatSettings;
import com.llamalad7.mixinextras.injector.ModifyExpressionValue;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.ChainBlock;
import net.minecraft.block.DirtPathBlock;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.BlockView;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

@Mixin(World.class)
public class WorldMixin {

    @Redirect(method = "updateComparators", at = @At(value = "INVOKE", target = "Lnet/minecraft/block/BlockState;isSolidBlock(Lnet/minecraft/world/BlockView;Lnet/minecraft/util/math/BlockPos;)Z"))
    private boolean addChainsToCheck(BlockState blockState, BlockView world, BlockPos blockPos) {
        boolean bl = false;
        Block block = blockState.getBlock();
        if (DoormatSettings.parityComparatorsReadThroughChains)
            bl = block instanceof ChainBlock;
        if (DoormatSettings.parityComparatorsReadThroughPaths)
            bl = block instanceof DirtPathBlock;
        return bl || blockState.isSolidBlock(world, blockPos);
    }

}
