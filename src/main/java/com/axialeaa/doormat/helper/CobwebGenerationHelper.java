package com.axialeaa.doormat.helper;

import com.axialeaa.doormat.DoormatSettings;
import net.minecraft.block.Blocks;
import net.minecraft.entity.Entity;
import net.minecraft.entity.mob.CaveSpiderEntity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.util.math.random.Random;

public class CobwebGenerationHelper {

    /**
     * For as long as the rule is enabled and the entity inside the spawner is a cave spider, check all positions in the specified area centred on the spawner for adjacent faces. If there are at least 2 adjacent faces, create a cobweb in that position with a commonness that scales with the number of adjacent faces.
     */
    public static void forBox(ServerWorld world, BlockPos pos, Entity storedEntity, int h, int v, int rarity) {
        if (DoormatSettings.spawnersGenerateCobwebs && storedEntity instanceof CaveSpiderEntity) {
            Random random = world.getRandom();

            for (BlockPos blockPos : BlockPos.iterate(pos.add(-h, -v, -h), pos.add(h, v, h))) {
                int i = 0;
                for (Direction direction : Direction.values()) {
                    if (world.getBlockState(blockPos).isReplaceable() && world.getBlockState(blockPos.offset(direction)).isSideSolidFullSquare(world, pos, direction.getOpposite())) {
                        i++;
//                        if (DoormatServer.IS_DEBUG)
//                            // Supporting faces
//                            RenderHandler.addCuboidFaceQuad(blockPos.offset(direction).toCenterPos(), 0.5001, RenderHandler.getColorWithAlpha(RenderHandler.getTrubetskoyColor("white"), 40), 200, true, direction);
                    }
                }
                if (i >= 2) {
//                    Color color = RenderHandler.getTrubetskoyColor("white");

                    if (random.nextInt(i + rarity) > rarity) {
                        world.setBlockState(blockPos, Blocks.COBWEB.getDefaultState());
//                        color = RenderHandler.getTrubetskoyColor("green");
                    }

//                    if (DoormatServer.IS_DEBUG)
//                        // Satisfactory placement spots
//                        RenderHandler.addCuboidLines(blockPos.toCenterPos(), 0.5, color, 200, true);
                }
            }

//            if (DoormatServer.IS_DEBUG)
//                // Enclosing box
//                RenderHandler.addCuboidLines(pos.getX() - h, pos.getY() - v, pos.getZ() - h, pos.getX() + h + 1, pos.getY() + v + 1, pos.getZ() + h + 1, RenderHandler.getTrubetskoyColor("purple"), 200, true);
        }
    }

    public static void forBox(ServerWorld world, BlockPos pos, Entity storedEntity, int spread, int rarity) {
        forBox(world, pos, storedEntity, spread, spread, rarity);
    }

}
