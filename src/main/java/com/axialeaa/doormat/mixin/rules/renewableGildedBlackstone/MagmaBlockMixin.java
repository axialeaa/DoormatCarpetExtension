package com.axialeaa.doormat.mixin.rules.renewableGildedBlackstone;

import com.axialeaa.doormat.DoormatSettings;
import com.axialeaa.doormat.mixin.extendables.AbstractBlockMixin;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.block.MagmaBlock;
import net.minecraft.fluid.Fluids;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.util.math.random.Random;
import net.minecraft.world.event.GameEvent;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(MagmaBlock.class)
public class MagmaBlockMixin extends AbstractBlockMixin {

    @Override
    public void injectedRandomTick(BlockState state, ServerWorld world, BlockPos pos, Random random, CallbackInfo ci) {
        if (DoormatSettings.renewableGildedBlackstone && world.getFluidState(pos.up()).isOf(Fluids.WATER))
            // if the rules is enabled and the block above is water...
            for (Direction direction : Direction.values()) {
                // iterate through a list of directions around the magma block
                BlockPos blockPos = pos.offset(direction);
                if (world.getBlockState(blockPos).isOf(Blocks.BLACKSTONE) && random.nextFloat() < 0.001) {
                    // if the block directly adjacent in this direction is blackstone and a random number between 0 and 1 is less than 0.001...
                    world.setBlockState(blockPos, Blocks.GILDED_BLACKSTONE.getDefaultState());
                    world.emitGameEvent(GameEvent.BLOCK_CHANGE, blockPos, GameEvent.Emitter.of(world.getBlockState(blockPos)));
                    // set the blackstone to gilded blackstone and emit a block change event
                }
            }
    }

}
