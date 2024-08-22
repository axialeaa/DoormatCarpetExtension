package com.axialeaa.doormat.mixin.rule.sneakingRandomTickChance;

import com.axialeaa.doormat.DoormatSettings;
import net.minecraft.block.BlockState;
import net.minecraft.entity.Entity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.random.Random;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(Entity.class)
public abstract class EntityMixin {

    @Shadow public abstract BlockPos getBlockPos();
    @Shadow public abstract World getWorld();

    @Inject(method = "setSneaking", at = @At("HEAD"))
    private void randomTickOnSneak(boolean sneaking, CallbackInfo ci) {
        if (DoormatSettings.sneakingRandomTickChance <= 0 || !sneaking)
            return;

        BlockPos blockPos = this.getBlockPos();

        for (BlockPos blockPos1 : BlockPos.iterate(blockPos.add(-3, -3, -3), blockPos.add(3, 3, 3))) {
            World world = this.getWorld();
            BlockState blockState = world.getBlockState(blockPos1);
            Random random = world.getRandom();

            if (random.nextFloat() < DoormatSettings.sneakingRandomTickChance && blockState.hasRandomTicks())
                blockState.randomTick((ServerWorld) world, blockPos1, random);
        }
    }

}
