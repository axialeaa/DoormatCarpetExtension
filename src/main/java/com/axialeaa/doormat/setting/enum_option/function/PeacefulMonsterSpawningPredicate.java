package com.axialeaa.doormat.setting.enum_option.function;

import net.minecraft.entity.SpawnReason;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

@FunctionalInterface
public interface PeacefulMonsterSpawningPredicate {

    boolean canSpawn(World world, BlockPos pos, SpawnReason reason);

}