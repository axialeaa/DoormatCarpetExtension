package com.axialeaa.doormat.mixin.rule.redstoneOpensBarrels;

import com.axialeaa.doormat.setting.DoormatSettings;
import com.axialeaa.doormat.helper.BarrelItemDumpingHelper;
import com.axialeaa.doormat.mixin.impl.AbstractBlockImplMixin;
import com.axialeaa.doormat.tinker_kit.TinkerKit;
import com.llamalad7.mixinextras.sugar.Local;
import net.minecraft.block.BarrelBlock;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.entity.BarrelBlockEntity;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvents;
import net.minecraft.state.property.BooleanProperty;
import net.minecraft.util.ActionResult;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.random.Random;
import net.minecraft.world.World;
import net.minecraft.world.tick.TickPriority;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(BarrelBlock.class)
public class BarrelBlockMixin extends AbstractBlockImplMixin {

    @Shadow @Final public static BooleanProperty OPEN;

    @Unique private boolean isPowered;

    @Inject(method = "onUse", at = @At(value = "INVOKE", target = "Lnet/minecraft/entity/player/PlayerEntity;openHandledScreen(Lnet/minecraft/screen/NamedScreenHandlerFactory;)Ljava/util/OptionalInt;", shift = At.Shift.BEFORE))
    private void ejectItemsOnUseOpen(BlockState state, World world, BlockPos pos, PlayerEntity player, BlockHitResult hit, CallbackInfoReturnable<ActionResult> cir, @Local BlockEntity blockEntity) {
        BarrelItemDumpingHelper.dumpItemStacks(world, pos, state, (BarrelBlockEntity) blockEntity);
    }

    /**
     * If the rule is enabled, flips the open blockstate of the barrel when it receives a neighbour update and the open state doesn't match whether or not it's powered.
     */
    @Override
    public void neighborUpdateImpl(BlockState state, World world, BlockPos pos, Block sourceBlock, BlockPos sourcePos, boolean notify, CallbackInfo ci) {
        if (!DoormatSettings.redstoneOpensBarrels)
            return;

        Block block = state.getBlock();
        int delay = TinkerKit.getDelay(block, 0);

        this.isPowered = TinkerKit.isReceivingRedstonePower(world, pos, block);

        if (delay > 0) {
            TickPriority tickPriority = TinkerKit.getTickPriority(block, TickPriority.NORMAL);
            world.scheduleBlockTick(pos, state.getBlock(), delay, tickPriority);
        }
        else getBehaviour(world, pos, state);
    }

    @Override
    public void scheduledTickImpl(BlockState state, ServerWorld world, BlockPos pos, Random random, CallbackInfo ci) {
        getBehaviour(world, pos, state);
    }

    @Unique
    public void getBehaviour(World world, BlockPos pos, BlockState state) {
        if (state.get(OPEN) == this.isPowered)
            return;

        int flags = TinkerKit.getFlags(state.getBlock(), Block.NOTIFY_ALL);
        world.setBlockState(pos, state.with(OPEN, this.isPowered), flags);

        if (state.get(OPEN) && world.getBlockEntity(pos) instanceof BarrelBlockEntity blockEntity)
            BarrelItemDumpingHelper.dumpItemStacks(world, pos, state, blockEntity);

        world.playSound(null, pos, isPowered ? SoundEvents.BLOCK_BARREL_OPEN : SoundEvents.BLOCK_BARREL_CLOSE, SoundCategory.BLOCKS);
    }

}