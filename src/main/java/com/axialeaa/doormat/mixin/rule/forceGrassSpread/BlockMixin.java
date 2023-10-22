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
public class BlockMixin implements Fertilizable {

    public boolean isFertilizable(WorldView world, BlockPos pos, BlockState state) {
        if (DoormatSettings.forceGrassSpread && world.getBlockState(pos).isOf(Blocks.DIRT)) {
            // if the rule is enabled and the block in question is dirt...
            if (!world.getBlockState(pos.up()).isTransparent(world, pos))
                return false; // if the block above the block in question is not transparent, return false immediately
            for (BlockPos blockPos : BlockPos.iterate(pos.add(-1, -1, -1), pos.add(1, 1, 1)))
                // iterate through a list of block positions in a box extending out 1 block from this dirt block
                if (world.getBlockState(blockPos).isOf(Blocks.GRASS_BLOCK) || world.getBlockState(blockPos).isOf(Blocks.MYCELIUM))
                    return true; // if the block in this position is grass or mycelium, tell the game to fertilize it
        }
        return false; // if all else fails, return false
    }

    @Override
    public boolean canGrow(World world, Random random, BlockPos pos, BlockState state) {
        return DoormatSettings.forceGrassSpread && state.isOf(Blocks.DIRT);
    }

    @Override
    public void grow(ServerWorld world, Random random, BlockPos pos, BlockState state) {
        boolean isGrass = false; // define two variables to change when the following iteration finds grass or mycelium
        boolean isMycelium = false;
        for (BlockPos blockPos : BlockPos.iterate(pos.add(-1, -1, -1), pos.add(1, 1, 1))) {
            // iterate through a list of block positions in a box extending out 1 block from this dirt block
            BlockState blockState = world.getBlockState(blockPos); // and get the block state at this position
            if (blockState.isOf(Blocks.GRASS_BLOCK))
                isGrass = true; // if the block is grass, reassign isGrass
            if (blockState.isOf(Blocks.MYCELIUM))
                isMycelium = true; // if the block is mycelium, reassign isMycelium
            if (!SpreadableBlock.canSpread(state, world, blockPos) || !isGrass || !isMycelium)
                // if the block position is not valid for spreading, or if either grass or mycelium have not been found yet...
                continue; // continue the loop
            break; // otherwise stop it
        }
        if (isGrass && isMycelium) // if both grass and mycelium have been found, choose randomly between grass and mycelium and set the dirt to this
            world.setBlockState(pos, random.nextBoolean() ? Blocks.GRASS_BLOCK.getDefaultState() : Blocks.MYCELIUM.getDefaultState(), Block.NOTIFY_ALL);
        else if (isGrass) // otherwise if grass has been found, set the dirt to grass
            world.setBlockState(pos, Blocks.GRASS_BLOCK.getDefaultState(), Block.NOTIFY_ALL);
        else if (isMycelium) // otherwise if mycelium has been found, set the dirt to mycelium
            world.setBlockState(pos, Blocks.MYCELIUM.getDefaultState(), Block.NOTIFY_ALL);
    }

}
