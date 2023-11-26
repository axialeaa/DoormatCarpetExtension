package com.axialeaa.doormat.helpers;

import com.axialeaa.doormat.DoormatSettings;
import net.minecraft.block.Blocks;
import net.minecraft.entity.Entity;
import net.minecraft.entity.mob.CaveSpiderEntity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.util.math.random.Random;

public class CobwebGeneration {

    /**
     * For as long as the rule is enabled and the entity inside the spawner is a cave spider, check all positions in the specified area centred on the spawner for adjacent faces. If there are at least 2 adjacent faces, create a cobweb in that position with a commonness that scales with the number of adjacent faces.
     */
    public static void forBox(ServerWorld world, BlockPos pos, Entity storedEntity, int horizontalSpread, int verticalSpread, int rarity) {
        Random random = world.getRandom();
        if (DoormatSettings.renewableCobwebs && storedEntity instanceof CaveSpiderEntity)
            for (BlockPos blockPos : BlockPos.iterate(pos.add(-horizontalSpread, -verticalSpread, -horizontalSpread), pos.add(horizontalSpread, verticalSpread, horizontalSpread))) {
                int i = 0;
                for (Direction direction : Direction.values())
                    if (world.getBlockState(blockPos).isAir() && world.getBlockState(blockPos.offset(direction)).isSideSolidFullSquare(world, pos, direction.getOpposite()))
                        i++;
                if (i >= 2 && random.nextInt(i + rarity) > rarity)
                    world.setBlockState(blockPos, Blocks.COBWEB.getDefaultState());
            }
    }

}
