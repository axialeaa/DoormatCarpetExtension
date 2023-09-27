package com.axialeaa.doormat.mixin.inheritance_parents;

import net.minecraft.block.AbstractBlock;
import net.minecraft.block.BlockState;
import net.minecraft.block.ShapeContext;
import net.minecraft.entity.Entity;
import net.minecraft.entity.ai.pathing.NavigationType;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.random.Random;
import net.minecraft.util.shape.VoxelShape;
import net.minecraft.world.BlockView;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(AbstractBlock.class)
public abstract class AbstractBlockMixin {

    @Inject(method = "getCollisionShape", at = @At("HEAD"))
    protected void injectedGetCollisionShape(BlockState state, BlockView world, BlockPos pos, ShapeContext context, CallbackInfoReturnable<VoxelShape> cir) {}

    @Inject(method = "getCameraCollisionShape", at = @At("HEAD"))
    protected void injectedGetCameraCollisionShape(BlockState state, BlockView world, BlockPos pos, ShapeContext context, CallbackInfoReturnable<VoxelShape> cir) {}

    @Inject(method = "canPathfindThrough", at = @At("HEAD"))
    protected void injectedCanPathfindThrough(BlockState state, BlockView world, BlockPos pos, NavigationType type, CallbackInfoReturnable<Boolean> cir) {}

    @Inject(method = "onBlockAdded", at = @At("HEAD"))
    public void injectedOnBlockAdded(BlockState state, World world, BlockPos pos, BlockState oldState, boolean notify, CallbackInfo ci) {}

    @Inject(method = "emitsRedstonePower", at = @At("HEAD"))
    public void injectedEmitsRedstonePower(BlockState state, CallbackInfoReturnable<Boolean> cir) {}

    @Inject(method = "randomTick", at = @At("HEAD"))
    public void injectedRandomTick(BlockState state, ServerWorld world, BlockPos pos, Random random, CallbackInfo ci) {}

    @Inject(method = "onEntityCollision", at = @At("HEAD"))
    public void injectedOnEntityCollision(BlockState state, World world, BlockPos pos, Entity entity, CallbackInfo ci) {}

}
