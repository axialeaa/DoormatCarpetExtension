package com.axialeaa.doormat.mixin.rule.grassAging;

import com.axialeaa.doormat.setting.DoormatSettings;
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
        if (DoormatSettings.grassAgingChance == 0 || random.nextFloat() >= DoormatSettings.grassAgingChance || !state.isOf(Blocks.GRASS_BLOCK))
            return;

        BlockPos upPos = pos.up();
        BlockState upState = world.getBlockState(upPos);

        if (upState.isAir())
            world.setBlockState(upPos, Blocks.SHORT_GRASS.getDefaultState());
        else if (upState.isOf(Blocks.SHORT_GRASS) && world.getBlockState(upPos.up()).isAir())
            TallPlantBlock.placeAt(world, Blocks.TALL_GRASS.getDefaultState(), upPos, SpreadableBlock.NOTIFY_LISTENERS);
    }

}
