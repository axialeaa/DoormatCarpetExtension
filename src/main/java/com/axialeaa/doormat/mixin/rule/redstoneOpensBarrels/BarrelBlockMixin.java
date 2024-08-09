package com.axialeaa.doormat.mixin.rule.redstoneOpensBarrels;

import com.axialeaa.doormat.DoormatSettings;
import com.axialeaa.doormat.fake.TinkerKitBehaviourSetter;
import com.axialeaa.doormat.mixin.extensibility.AbstractBlockMixin;
import com.axialeaa.doormat.tinker_kit.TinkerKit;
import net.minecraft.block.BarrelBlock;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvents;
import net.minecraft.state.property.BooleanProperty;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.random.Random;
import net.minecraft.world.World;
import net.minecraft.world.tick.TickPriority;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(BarrelBlock.class)
public class BarrelBlockMixin extends AbstractBlockMixin implements TinkerKitBehaviourSetter {

    @Shadow @Final public static BooleanProperty OPEN;

    @Unique private boolean isPowered;

    /**
     * If the rule is enabled, flips the open blockstate of the barrel when it receives a neighbour update and the open state doesn't match whether or not it's powered.
     */
    @Override
    public void neighborUpdateImpl(BlockState state, World world, BlockPos pos, Block sourceBlock, BlockPos sourcePos, boolean notify, CallbackInfo ci) {
        if (DoormatSettings.redstoneOpensBarrels) {
            this.isPowered = TinkerKit.isReceivingRedstonePower(world, pos, state);
            int delay = TinkerKit.getDelay(state, 0);

            if (delay == 0)
                getBehaviour(world, pos, state);
            else {
                TickPriority tickPriority = TinkerKit.getTickPriority(state, TickPriority.NORMAL);
                world.scheduleBlockTick(pos, state.getBlock(), delay, tickPriority);
            }
        }
    }

    @Override
    public void scheduledTickImpl(BlockState state, ServerWorld world, BlockPos pos, Random random, CallbackInfo ci) {
        getBehaviour(world, pos, state);
    }

    @Override
    public void getBehaviour(World world, BlockPos pos, BlockState state) {
        if (state.get(OPEN) != isPowered) {
            int flags = TinkerKit.getFlags(state, Block.NOTIFY_ALL);
            world.setBlockState(pos, state.with(BarrelBlock.OPEN, isPowered), flags);

            world.playSound(null, pos, isPowered ? SoundEvents.BLOCK_BARREL_OPEN : SoundEvents.BLOCK_BARREL_CLOSE, SoundCategory.BLOCKS);
        }
    }

}