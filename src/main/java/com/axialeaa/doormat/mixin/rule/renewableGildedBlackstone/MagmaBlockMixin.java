package com.axialeaa.doormat.mixin.rule.renewableGildedBlackstone;

import com.axialeaa.doormat.DoormatSettings;
import com.axialeaa.doormat.mixin.extensibility.AbstractBlockMixin;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.block.MagmaBlock;
import net.minecraft.fluid.FluidState;
import net.minecraft.fluid.Fluids;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.util.math.random.Random;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(MagmaBlock.class)
public class MagmaBlockMixin extends AbstractBlockMixin {

    /**
     * As long as the rule is enabled and the block above the magma block is water, replace blackstone for each adjacent direction with gilded blackstone.
     */
    @Override
    public void randomTickImpl(BlockState state, ServerWorld world, BlockPos pos, Random random, CallbackInfo ci) {
        FluidState upFluidState = world.getFluidState(pos.up());

        if (DoormatSettings.renewableGildedBlackstone <= 0 || !upFluidState.isOf(Fluids.WATER))
            return;

        for (Direction direction : Direction.values()) {
            BlockPos blockPos = pos.offset(direction);
            BlockState blockState = world.getBlockState(blockPos);

            if (blockState.isOf(Blocks.BLACKSTONE) && random.nextFloat() < DoormatSettings.renewableGildedBlackstone)
                world.setBlockState(blockPos, Blocks.GILDED_BLACKSTONE.getDefaultState());
        }
    }

}
