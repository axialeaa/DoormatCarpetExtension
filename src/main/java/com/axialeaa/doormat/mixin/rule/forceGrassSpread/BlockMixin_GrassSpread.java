package com.axialeaa.doormat.mixin.rule.forceGrassSpread;

import com.axialeaa.doormat.DoormatSettings;
import net.minecraft.block.*;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.random.Random;
import net.minecraft.world.World;
import net.minecraft.world.WorldView;
import org.spongepowered.asm.mixin.Mixin;

@Mixin(Block.class)
public class BlockMixin_GrassSpread implements Fertilizable {

    public boolean isFertilizable(WorldView world, BlockPos pos, BlockState state) {
        if (DoormatSettings.forceGrassSpread && world.getBlockState(pos).isOf(Blocks.DIRT)) {
            if (!world.getBlockState(pos.up()).isTransparent(world, pos)) return false;
            for (BlockPos blockPos : BlockPos.iterate(pos.add(-1, -1, -1), pos.add(1, 1, 1))) {
                if (!world.getBlockState(blockPos).isOf(Blocks.GRASS_BLOCK) && !world.getBlockState(blockPos).isOf(Blocks.MYCELIUM)) continue;
                return true;
            }
        }
        return false;
    }

    @Override
    public boolean canGrow(World world, Random random, BlockPos pos, BlockState state) {
        return DoormatSettings.forceGrassSpread && state.isOf(Blocks.DIRT);
    }

    @Override
    public void grow(ServerWorld world, Random random, BlockPos pos, BlockState state) {
        boolean isMycelium = false;
        boolean isGrass = false;
        for (BlockPos blockPos : BlockPos.iterate(pos.add(-1, -1, -1), pos.add(1, 1, 1))) {
            BlockState blockState = world.getBlockState(blockPos);
            if (blockState.isOf(Blocks.GRASS_BLOCK)) {
                isGrass = true;
            }
            if (blockState.isOf(Blocks.MYCELIUM)) {
                isMycelium = true;
            }
            if (!SpreadableBlock.canSpread(state, world, pos) || !isGrass || !isMycelium) continue;
            break;
        }
        if (isGrass && isMycelium) {
            world.setBlockState(pos, random.nextBoolean() ? Blocks.GRASS_BLOCK.getDefaultState() : Blocks.MYCELIUM.getDefaultState(), Block.NOTIFY_ALL);
        } else if (isGrass) {
            world.setBlockState(pos, Blocks.GRASS_BLOCK.getDefaultState(), Block.NOTIFY_ALL);
        } else if (isMycelium) {
            world.setBlockState(pos, Blocks.MYCELIUM.getDefaultState(), Block.NOTIFY_ALL);
        }
    }

}
