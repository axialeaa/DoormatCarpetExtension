package com.axialeaa.doormat.mixin.block.multiple_categories;

import com.axialeaa.doormat.DoormatSettings;
import com.axialeaa.doormat.mixin.inheritance_parents.AbstractBlockMixin;
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
public class LeavesBlockMixin_GrowFlowers_Collision extends AbstractBlockMixin implements Fertilizable {

     // leavesNoCollision

    @Override
    public void injectedGetCollisionShape(BlockState state, BlockView world, BlockPos pos, ShapeContext context, CallbackInfoReturnable<VoxelShape> cir) {
        if (DoormatSettings.leavesNoCollision) cir.setReturnValue(VoxelShapes.empty());
    }

    @Override
    public void injectedGetCameraCollisionShape(BlockState state, BlockView world, BlockPos pos, ShapeContext context, CallbackInfoReturnable<VoxelShape> cir) {
        if (DoormatSettings.leavesNoCollision) cir.setReturnValue(VoxelShapes.empty());
    }

    @Override
    public void injectedCanPathfindThrough(BlockState state, BlockView world, BlockPos pos, NavigationType type, CallbackInfoReturnable<Boolean> cir) {
        if (DoormatSettings.leavesNoCollision) cir.setReturnValue(true);
    }

    // azaleaLeavesGrowFlowers

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
