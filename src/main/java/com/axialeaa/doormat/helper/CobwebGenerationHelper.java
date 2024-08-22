package com.axialeaa.doormat.helper;

import com.axialeaa.doormat.DoormatSettings;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.entity.Entity;
import net.minecraft.entity.mob.CaveSpiderEntity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.util.math.random.Random;
import net.minecraft.world.World;

public class CobwebGenerationHelper {

    /**
     * For as long as the rule is enabled and the entity inside the spawner is a cave spider, check all positions in the specified area centred on the spawner for adjacent faces. If there are at least 2 adjacent faces, create a cobweb in that position with a commonness that scales with the number of adjacent faces.
     */
    public static void forBox(World world, BlockPos pos, Entity storedEntity, int hSize, int vSize, int rarity) {
        if (!DoormatSettings.spawnersGenerateCobwebs || !(storedEntity instanceof CaveSpiderEntity))
            return;

        for (BlockPos blockPos : BlockPos.iterate(pos.add(-hSize, -vSize, -hSize), pos.add(hSize, vSize, hSize)))
            placeCobweb(world, blockPos, rarity);
    }

    public static void forBox(ServerWorld world, BlockPos pos, Entity storedEntity, int spread, int rarity) {
        forBox(world, pos, storedEntity, spread, spread, rarity);
    }

    private static void placeCobweb(World world, BlockPos pos, int rarity) {
        int i = 0;
        Random random = world.getRandom();

        for (Direction direction : Direction.values()) {
            BlockState blockState = world.getBlockState(pos);
            BlockState offsetState = world.getBlockState(pos.offset(direction));

            if (blockState.isReplaceable() && offsetState.isSideSolidFullSquare(world, pos, direction.getOpposite()))
                i++;
        }

        if (i >= 2 && random.nextInt(i + rarity) > rarity)
            world.setBlockState(pos, Blocks.COBWEB.getDefaultState());
    }

}
