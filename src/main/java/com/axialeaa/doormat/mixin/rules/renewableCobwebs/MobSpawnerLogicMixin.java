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

    @Inject(method = "serverTick", at = @At(value = "INVOKE", target = "Lnet/minecraft/server/world/ServerWorld;spawnNewEntityAndPassengers(Lnet/minecraft/entity/Entity;)Z"))
    private void generateCobwebs(ServerWorld world, BlockPos pos, CallbackInfo ci, @Local Entity storedEntity) {
        Random random = world.getRandom();
        if (DoormatSettings.renewableCobwebs && storedEntity instanceof CaveSpiderEntity) // if the rules is enabled and the stored entity is a cave spider...
            for (BlockPos blockPos : BlockPos.iterate(pos.add(-this.spawnRange, -this.spawnRange, -this.spawnRange), pos.add(this.spawnRange, this.spawnRange, this.spawnRange))) {
                // for every block position in a box that extends out from the spawner by a number of blocks equal to the spawn range (compatible with mods that tweak the spawn range)...
                int i = 0; // define a new integer
                for (Direction direction : Direction.values()) // and check the blocks adjacent to the block position in every direction
                    if (world.getBlockState(blockPos).isAir() && world.getBlockState(blockPos.offset(direction)).isSideSolidFullSquare(world, pos, direction.getOpposite()))
                        i++; // if the block position is air and the block in the checked direction can feasibly "support" cobwebs, increment the integer by 1
                if (i >= 2 && random.nextInt(i + 128) > 128)
                    world.setBlockState(blockPos, Blocks.COBWEB.getDefaultState());
                    // if the integer is greater than or equal to 2 (in essence, if there are 2 or more supporting blocks around the checked block position)
                    // and a random integer between 0 inc. and the number of supporting sides + 128 exc. is greater than 128, place a cobweb here!
            }
    }

}