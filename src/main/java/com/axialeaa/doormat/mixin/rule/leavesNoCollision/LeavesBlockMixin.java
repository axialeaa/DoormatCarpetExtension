package com.axialeaa.doormat.mixin.rule.leavesNoCollision;

import com.axialeaa.doormat.DoormatSettings;
import com.axialeaa.doormat.mixin.AbstractBlockMixin;
import net.minecraft.block.BlockState;
import net.minecraft.block.LeavesBlock;
import net.minecraft.block.ShapeContext;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.shape.VoxelShape;
import net.minecraft.util.shape.VoxelShapes;
import net.minecraft.world.BlockView;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(LeavesBlock.class)
public class LeavesBlockMixin extends AbstractBlockMixin {

    @Override
    public void injectedGetCollisionShape(BlockState state, BlockView world, BlockPos pos, ShapeContext context, CallbackInfoReturnable<VoxelShape> cir) {
        if (DoormatSettings.leavesNoCollision)
            cir.setReturnValue(VoxelShapes.empty());
    }

}
