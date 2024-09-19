package com.axialeaa.doormat.mixin.impl;

import com.llamalad7.mixinextras.injector.wrapmethod.WrapMethod;
import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import net.minecraft.block.AbstractBlock;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.ShapeContext;
import net.minecraft.entity.Entity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.random.Random;
import net.minecraft.util.shape.VoxelShape;
import net.minecraft.world.BlockView;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;

/**
 * The purpose behind this mixin is avoiding extending + overriding of the base methods which would be otherwise be incompatible with any mod which tries modifying the same things. Instead, create an empty injection for each method, and extend + override the handler methods from this mixin. That way, method calls are instantiated without clunky instanceof checks and without incompatibility.
 * @implNote <strong>Please avoid using {@link org.spongepowered.asm.mixin.Shadow @Shadow} and {@link org.spongepowered.asm.mixin.Unique @Unique} here. If necessary, cast the mixin instance to the target class instance using {@link Class#cast(Object)}.</strong>
 */
@Mixin(AbstractBlock.class)
public abstract class AbstractBlockImplMixin {

    @WrapMethod(method = "getCollisionShape")
    public VoxelShape getCollisionShapeImpl(BlockState state, BlockView world, BlockPos pos, ShapeContext context, Operation<VoxelShape> original) {
        return original.call(state, world, pos, context);
    }

    @WrapMethod(method = "onBlockAdded")
    public void onBlockAddedImpl(BlockState state, World world, BlockPos pos, BlockState oldState, boolean notify, Operation<Void> original) {
        original.call(state, world, pos, oldState, notify);
    }

    @WrapMethod(method = "randomTick")
    public void randomTickImpl(BlockState state, ServerWorld world, BlockPos pos, Random random, Operation<Void> original) {
        original.call(state, world, pos, random);
    }

    @WrapMethod(method = "onEntityCollision")
    public void onEntityCollisionImpl(BlockState state, World world, BlockPos pos, Entity entity, Operation<Void> original) {
        original.call(state, world, pos, entity);
    }

    @WrapMethod(method = "neighborUpdate")
    public void neighborUpdateImpl(BlockState state, World world, BlockPos pos, Block sourceBlock, BlockPos sourcePos, boolean notify, Operation<Void> original) {
        original.call(state, world, pos, sourceBlock, sourcePos, notify);
    }

    @WrapMethod(method = "scheduledTick")
    public void scheduledTickImpl(BlockState state, ServerWorld world, BlockPos pos, Random random, Operation<Void> original) {
        original.call(state, world, pos, random);
    }

}
