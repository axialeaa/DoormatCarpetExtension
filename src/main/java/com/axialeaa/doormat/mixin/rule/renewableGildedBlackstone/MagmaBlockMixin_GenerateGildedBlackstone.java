package com.axialeaa.doormat.mixin.rule.renewableGildedBlackstone;

import com.axialeaa.doormat.DoormatSettings;
import com.axialeaa.doormat.mixin.AbstractBlockMixin;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.block.MagmaBlock;
import net.minecraft.fluid.Fluids;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.util.math.random.Random;
import net.minecraft.world.event.GameEvent;
import org.spongepowered.asm.mixin.Debug;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Debug(export = true)
@Mixin(MagmaBlock.class)
public class MagmaBlockMixin_GenerateGildedBlackstone extends AbstractBlockMixin {

    @Override
    public void injectedRandomTick(BlockState state, ServerWorld world, BlockPos pos, Random random, CallbackInfo ci) {
        if (DoormatSettings.renewableGildedBlackstone && world.getFluidState(pos.up()).isOf(Fluids.WATER)) {
            for (Direction direction : Direction.values()) {
                BlockPos offsetBlockPos = pos.offset(direction);
                if (world.getBlockState(offsetBlockPos).isOf(Blocks.BLACKSTONE) && random.nextFloat() < 0.001) {
                    world.emitGameEvent(GameEvent.BLOCK_CHANGE, offsetBlockPos, GameEvent.Emitter.of(world.getBlockState(offsetBlockPos)));
                    world.setBlockState(offsetBlockPos, Blocks.GILDED_BLACKSTONE.getDefaultState());
                }
            }
        }
    }

}
