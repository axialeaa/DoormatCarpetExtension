package com.axialeaa.doormat.mixin.rules.forceGrassSpread;

import com.axialeaa.doormat.DoormatSettings;
import net.minecraft.block.*;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.random.Random;
import net.minecraft.world.World;
import net.minecraft.world.WorldView;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;

@Mixin(Block.class)
public class BlockMixin implements Fertilizable {

    @Unique
    private boolean isRuleEnabledAndDirt(WorldView world, BlockPos pos) {
        return DoormatSettings.forceGrassSpread && world.getBlockState(pos).isOf(Blocks.DIRT);
    }

    /**
     * @return true if the rule is enabled and there is at least 1 grass or mycelium block in a 3x3x3 cube centred on an air-exposed dirt block in question, otherwise false.
     */
    @Override
    public boolean isFertilizable(WorldView world, BlockPos pos, BlockState state) {
        if (isRuleEnabledAndDirt(world, pos)) {
            if (!world.getBlockState(pos.up()).isTransparent(world, pos))
                return false;
            for (BlockPos blockPos : BlockPos.iterate(pos.add(-1, -1, -1), pos.add(1, 1, 1)))
                return world.getBlockState(blockPos).isOf(Blocks.GRASS_BLOCK) || world.getBlockState(blockPos).isOf(Blocks.MYCELIUM);
        }
        return false;
    }

    @Override
    public boolean canGrow(World world, Random random, BlockPos pos, BlockState state) {
        return isRuleEnabledAndDirt(world, pos);
    }

    /**
     * Creates a list of grass and mycelium blocks in a 3x3x3 cube centred on the dirt block in question. If this list contains a grass block OR mycelium and the conditions are valid for spreading, turn the dirt into that block. If there are at least 1 of each in the cube, it picks randomly between them.
     */
    @Override
    public void grow(ServerWorld world, Random random, BlockPos pos, BlockState state) {
        boolean isGrass = false;
        boolean isMycelium = false;
        for (BlockPos blockPos : BlockPos.iterate(pos.add(-1, -1, -1), pos.add(1, 1, 1))) {
            BlockState blockState = world.getBlockState(blockPos);
            if (blockState.isOf(Blocks.GRASS_BLOCK))
                isGrass = true;
            if (blockState.isOf(Blocks.MYCELIUM))
                isMycelium = true;
            if (!SpreadableBlock.canSpread(state, world, blockPos) || !isGrass || !isMycelium) continue;
            break;
        }
        if (isGrass && isMycelium)
            world.setBlockState(pos, random.nextBoolean() ? Blocks.GRASS_BLOCK.getDefaultState() : Blocks.MYCELIUM.getDefaultState());
        else if (isGrass)
            world.setBlockState(pos, Blocks.GRASS_BLOCK.getDefaultState());
        else if (isMycelium)
            world.setBlockState(pos, Blocks.MYCELIUM.getDefaultState());
    }

}
