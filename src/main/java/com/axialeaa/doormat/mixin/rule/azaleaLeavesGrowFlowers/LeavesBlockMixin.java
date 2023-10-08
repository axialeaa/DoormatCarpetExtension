package com.axialeaa.doormat.mixin.rule.azaleaLeavesGrowFlowers;

import com.axialeaa.doormat.DoormatSettings;
import com.axialeaa.doormat.mixin.AbstractBlockMixin;
import net.minecraft.block.*;
import net.minecraft.entity.ai.pathing.NavigationType;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.random.Random;
import net.minecraft.util.shape.VoxelShape;
import net.minecraft.util.shape.VoxelShapes;
import net.minecraft.world.BlockView;
import net.minecraft.world.World;
import net.minecraft.world.WorldView;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(LeavesBlock.class)
public class LeavesBlockMixin implements Fertilizable {

    @Override
    public boolean isFertilizable(WorldView world, BlockPos pos, BlockState state) {
        return DoormatSettings.azaleaLeavesGrowFlowers && state.isOf(Blocks.AZALEA_LEAVES);
    }

    @Override
    public boolean canGrow(World world, Random random, BlockPos pos, BlockState state) {
        return DoormatSettings.azaleaLeavesGrowFlowers && state.isOf(Blocks.AZALEA_LEAVES);
    }

    @Override
    public void grow(ServerWorld world, Random random, BlockPos pos, BlockState state) {
        world.setBlockState(pos, Blocks.FLOWERING_AZALEA_LEAVES.getStateWithProperties(state));
    }

}
