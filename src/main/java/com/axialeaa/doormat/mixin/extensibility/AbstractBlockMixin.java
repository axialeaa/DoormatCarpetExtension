package com.axialeaa.doormat.mixin.extensibility;

import net.minecraft.block.AbstractBlock;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.ShapeContext;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.Hand;
import net.minecraft.util.ItemActionResult;
import net.minecraft.util.hit.BlockHitResult;
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

/**
 * The purpose behind this mixin is avoiding extending + overriding of the base methods which would be otherwise be incompatible with any mod which tries modifying the same things.
 * Instead, create an empty injection for each method, and extend + override the handler methods from this mixin.
 * That way, method calls are instantiated without clunky instanceof checks and without incompatibility.
 */
@SuppressWarnings({ "CancellableInjectionUsage", "EmptyMethod" })
@Mixin(AbstractBlock.class)
public abstract class AbstractBlockMixin {

    @Inject(method = "getCollisionShape", at = @At("HEAD"), cancellable = true)
    public void getCollisionShapeImpl(BlockState state, BlockView world, BlockPos pos, ShapeContext context, CallbackInfoReturnable<VoxelShape> cir) {}

    @Inject(method = "onBlockAdded", at = @At("HEAD"))
    public void onBlockAddedImpl(BlockState state, World world, BlockPos pos, BlockState oldState, boolean notify, CallbackInfo ci) {}

    @Inject(method = "randomTick", at = @At("HEAD"))
    public void randomTickImpl(BlockState state, ServerWorld world, BlockPos pos, Random random, CallbackInfo ci) {}

    @Inject(method = "onEntityCollision", at = @At("HEAD"))
    public void onEntityCollisionImpl(BlockState state, World world, BlockPos pos, Entity entity, CallbackInfo ci) {}

    @Inject(method = "neighborUpdate", at = @At("HEAD"))
    public void neighborUpdateImpl(BlockState state, World world, BlockPos pos, Block sourceBlock, BlockPos sourcePos, boolean notify, CallbackInfo ci) {}

    @Inject(method = "scheduledTick", at = @At("HEAD"))
    public void scheduledTickImpl(BlockState state, ServerWorld world, BlockPos pos, Random random, CallbackInfo ci) {}

    @Inject(method = "onUseWithItem", at = @At("HEAD"), cancellable = true)
    public void onUseWithItemImpl(ItemStack stack, BlockState state, World world, BlockPos pos, PlayerEntity player, Hand hand, BlockHitResult hit, CallbackInfoReturnable<ItemActionResult> cir) {}

}
