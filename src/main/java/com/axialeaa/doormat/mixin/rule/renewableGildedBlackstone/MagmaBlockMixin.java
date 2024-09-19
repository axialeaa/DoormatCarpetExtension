package com.axialeaa.doormat.mixin.rule.renewableGildedBlackstone;

import com.axialeaa.doormat.mixin.impl.AbstractBlockImplMixin;
import com.axialeaa.doormat.setting.DoormatSettings;
import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
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

@Mixin(MagmaBlock.class)
public class MagmaBlockMixin extends AbstractBlockImplMixin {

    @Override
    public void randomTickImpl(BlockState state, ServerWorld world, BlockPos pos, Random random, Operation<Void> original) {
        FluidState fluidState = world.getFluidState(pos.up());

        if (DoormatSettings.renewableGildedBlackstone <= 0 || !fluidState.isOf(Fluids.WATER))
            return;

        for (Direction direction : Direction.values()) {
            BlockPos blockPos = pos.offset(direction);
            BlockState blockState = world.getBlockState(blockPos);

            if (blockState.isOf(Blocks.BLACKSTONE) && random.nextFloat() < DoormatSettings.renewableGildedBlackstone)
                world.setBlockState(blockPos, Blocks.GILDED_BLACKSTONE.getDefaultState());
        }

        original.call(state, world, pos, random);
    }

}
