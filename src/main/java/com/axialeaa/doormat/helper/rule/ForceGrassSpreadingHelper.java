package com.axialeaa.doormat.helper.rule;

import com.axialeaa.doormat.DoormatSettings;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.block.SpreadableBlock;
import net.minecraft.client.util.ParticleUtil;
import net.minecraft.particle.ParticleTypes;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.random.Random;
import net.minecraft.world.World;

import java.util.ArrayList;
import java.util.List;

public class ForceGrassSpreadingHelper {

    /**
     * Future-proof implementation of what is essentially {@link net.minecraft.block.NetherrackBlock#grow(ServerWorld, Random, BlockPos, BlockState) nylium logic}. Instead of defining new booleans for each instanceof {@link SpreadableBlock}, just create a list of all {@link SpreadableBlock} instances around the target dirt, eliminate duplicate list entries, and pick a random list index. This should comfortably scale with any new {@link SpreadableBlock} types Mojang may or may not decide to add in the future.
     */
    public static boolean onUse(World world, BlockPos pos) {
        BlockState state = world.getBlockState(pos);

        if (DoormatSettings.forceGrassSpreading && state.isOf(Blocks.DIRT)) {
            List<Block> blocks = new ArrayList<>();

            for (BlockPos adjacentPos : BlockPos.iterate(pos.add(-1, -1, -1), pos.add(1, 1, 1))) {
                Block adjacentBlock = world.getBlockState(adjacentPos).getBlock();
                if (adjacentBlock instanceof SpreadableBlock)
                    blocks.add(adjacentBlock);
            }

            List<Block> uniqueBlocks = blocks.stream().distinct().toList();
            if (!uniqueBlocks.isEmpty() && SpreadableBlock.canSpread(state, world, pos)) {
                if (!world.isClient()) {
                    Block randomBlock = uniqueBlocks.get(world.getRandom().nextInt(uniqueBlocks.size()));
                    boolean isSnowAbove = world.getBlockState(pos.up()).isOf(Blocks.SNOW);

                    world.setBlockState(pos, randomBlock.getDefaultState().with(SpreadableBlock.SNOWY, isSnowAbove));
                }
                ParticleUtil.spawnParticlesAround(world, pos.up(), 45, 3.0, 1.0, false, ParticleTypes.HAPPY_VILLAGER);
                return true;
            }
        }
        return false;
    }

}
