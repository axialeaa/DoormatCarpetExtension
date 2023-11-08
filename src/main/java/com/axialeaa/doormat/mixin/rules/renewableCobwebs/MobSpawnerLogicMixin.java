package com.axialeaa.doormat.mixin.rules.renewableCobwebs;

import com.axialeaa.doormat.DoormatSettings;
import com.llamalad7.mixinextras.sugar.Local;
import net.minecraft.block.Blocks;
import net.minecraft.entity.Entity;
import net.minecraft.entity.mob.CaveSpiderEntity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.util.math.random.Random;
import net.minecraft.world.MobSpawnerLogic;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(MobSpawnerLogic.class)
public abstract class MobSpawnerLogicMixin {

    @Shadow private int spawnRange;

    // oh boy, this is quite insane

    /**
     * As long as the rule is enabled and the entity inside the spawner is a cave spider, check all positions in a 9x9x9 area centred on the spawner for adjacent faces, every spawn cycle. If there are at least 2 adjacent faces, create a cobweb in that position with a commonness that scales with the number of adjacent faces up to about a 4.7% chance.
     */
    @Inject(method = "serverTick", at = @At(value = "INVOKE", target = "Lnet/minecraft/server/world/ServerWorld;spawnNewEntityAndPassengers(Lnet/minecraft/entity/Entity;)Z"))
    private void generateCobwebsOnSpawnCycle(ServerWorld world, BlockPos pos, CallbackInfo ci, @Local Entity storedEntity) {
        Random random = world.getRandom();
        if (DoormatSettings.renewableCobwebs && storedEntity instanceof CaveSpiderEntity)
            for (BlockPos blockPos : BlockPos.iterate(pos.add(-this.spawnRange, -this.spawnRange, -this.spawnRange), pos.add(this.spawnRange, this.spawnRange, this.spawnRange))) {
                int i = 0;
                for (Direction direction : Direction.values())
                    if (world.getBlockState(blockPos).isAir() && world.getBlockState(blockPos.offset(direction)).isSideSolidFullSquare(world, pos, direction.getOpposite()))
                        i++;
                if (i >= 2 && random.nextInt(i + 128) > 128)
                    world.setBlockState(blockPos, Blocks.COBWEB.getDefaultState());
            }
    }

}