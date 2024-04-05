package com.axialeaa.doormat.helper.rule;

import com.axialeaa.doormat.DoormatServer;
import com.axialeaa.doormat.DoormatSettings;
import com.axialeaa.doormat.util.RenderHandler;
import net.minecraft.block.Blocks;
import net.minecraft.entity.Entity;
import net.minecraft.entity.mob.CaveSpiderEntity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.util.math.Vec3d;
import net.minecraft.util.math.random.Random;

public class CobwebGenerationHelper {

    /**
     * For as long as the rule is enabled and the entity inside the spawner is a cave spider, check all positions in the specified area centred on the spawner for adjacent faces. If there are at least 2 adjacent faces, create a cobweb in that position with a commonness that scales with the number of adjacent faces.
     */
    public static void forBox(ServerWorld world, BlockPos pos, Entity storedEntity, int h, int v, int rarity) {
        if (DoormatSettings.spawnersGenerateCobwebs && storedEntity instanceof CaveSpiderEntity) {
            Random random = world.getRandom();

            BlockPos.iterate(pos.add(-h, -v, -h), pos.add(h, v, h)).forEach(blockPos -> {
                int i = 0;
                for (Direction direction : Direction.values())
                    if (world.getBlockState(blockPos).isAir() && world.getBlockState(blockPos.offset(direction)).isSideSolidFullSquare(world, pos, direction.getOpposite())) {
                        i++;
                        if (DoormatServer.IS_DEBUG)
                            RenderHandler.addCuboidFilled(Vec3d.ofCenter(blockPos).offset(direction, 0.5), 0.02, 0xFFFFFFFF, 0xFFFFFFFF, 10000);
                    }
                if (i >= 2) {
                    if (random.nextInt(i + rarity) > rarity)
                        world.setBlockState(blockPos, Blocks.COBWEB.getDefaultState());

                    if (DoormatServer.IS_DEBUG)
                        RenderHandler.addCuboidFilled(Vec3d.ofCenter(blockPos), 0.5, 0x2030FF30, 0xFF30FF30, 10000);
                }
            });

            if (DoormatServer.IS_DEBUG)
                RenderHandler.addCuboidWireFrame(pos.getX() - h, pos.getY() - v, pos.getZ() - h, pos.getX() + h + 1, pos.getY() + v + 1, pos.getZ() + h + 1, 0xFFA230FF, 10000);
        }
    }

    public static void forBox(ServerWorld world, BlockPos pos, Entity storedEntity, int spread, int rarity) {
        forBox(world, pos, storedEntity, spread, spread, rarity);
    }

}
