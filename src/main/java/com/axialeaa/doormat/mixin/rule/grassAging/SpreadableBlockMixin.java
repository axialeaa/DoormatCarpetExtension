package com.axialeaa.doormat.mixin.rule.grassAging;

import com.axialeaa.doormat.DoormatSettings;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.block.SpreadableBlock;
import net.minecraft.block.TallPlantBlock;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.random.Random;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(SpreadableBlock.class)
public class SpreadableBlockMixin {

    @Inject(method = "randomTick", at = @At("HEAD"))
    private void growLongGrass(BlockState state, ServerWorld world, BlockPos pos, Random random, CallbackInfo ci) {
        if (DoormatSettings.grassAgingChance > 0 && state.isOf(Blocks.GRASS_BLOCK) && random.nextFloat() < DoormatSettings.grassAgingChance) {
            BlockPos blockPos = pos.up();
            if (world.getBlockState(blockPos).isAir())
                world.setBlockState(blockPos, Blocks.SHORT_GRASS.getDefaultState());
            else if (world.getBlockState(blockPos).isOf(Blocks.SHORT_GRASS) && world.getBlockState(blockPos.up()).isAir())
                TallPlantBlock.placeAt(world, Blocks.TALL_GRASS.getDefaultState(), blockPos, 2);
        }
    }

}
