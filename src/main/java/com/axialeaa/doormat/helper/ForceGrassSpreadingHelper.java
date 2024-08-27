package com.axialeaa.doormat.helper;

import com.axialeaa.doormat.settings.DoormatSettings;
import com.axialeaa.doormat.mixin.rule.forceGrassSpreading.SpreadableBlockAccessor;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.block.SpreadableBlock;
import net.minecraft.particle.ParticleTypes;
import net.minecraft.particle.ParticleUtil;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.random.Random;
import net.minecraft.world.World;

import java.util.ArrayList;
import java.util.List;

public class ForceGrassSpreadingHelper {

    /**
     * Future-proof implementation of what is essentially {@link net.minecraft.block.NetherrackBlock#grow(ServerWorld, Random, BlockPos, BlockState) nylium logic}. Instead of defining new booleans for each instance of {@link SpreadableBlock}, just create a list of all {@link SpreadableBlock} instances around the target dirt, eliminate duplicate list entries, and pick a random list index. This should comfortably scale with any new {@link SpreadableBlock} types Mojang may or may not decide to add in the future.
     */
    @SuppressWarnings({"ConstantValue", "UnreachableCode"})
    public static boolean onUse(World world, BlockPos pos, BlockState state) {
        if (!DoormatSettings.forceGrassSpreading || !state.isOf(Blocks.DIRT))
            return false;

        List<Block> blocks = new ArrayList<>();

        for (BlockPos adjacentPos : BlockPos.iterate(pos.add(-1, -1, -1), pos.add(1, 1, 1))) {
            BlockState adjacentState = world.getBlockState(adjacentPos);
            Block adjacentBlock = adjacentState.getBlock();

            if (adjacentBlock instanceof SpreadableBlock)
                blocks.add(adjacentBlock);
        }

        List<Block> uniqueBlocks = blocks.stream().distinct().toList();

        if (uniqueBlocks.isEmpty() || !SpreadableBlockAccessor.invokeCanSpread(state, world, pos))
            return false;

        BlockPos upPos = pos.up();
        BlockState upState = world.getBlockState(upPos);

        ParticleUtil.spawnParticlesAround(world, upPos, 45, 3.0, 1.0, false, ParticleTypes.HAPPY_VILLAGER);

        if (!world.isClient()) {
            Block randomBlock = uniqueBlocks.get(world.getRandom().nextInt(uniqueBlocks.size()));
            boolean isSnowAbove = upState.isOf(Blocks.SNOW);

            world.setBlockState(pos, randomBlock.getDefaultState().with(SpreadableBlock.SNOWY, isSnowAbove));
        }

        return true;
    }

}
