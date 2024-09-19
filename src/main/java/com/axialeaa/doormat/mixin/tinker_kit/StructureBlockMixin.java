package com.axialeaa.doormat.mixin.tinker_kit;

import com.axialeaa.doormat.mixin.impl.AbstractBlockImplMixin;
import com.axialeaa.doormat.registry.DoormatTinkerTypes;
import com.axialeaa.doormat.tinker_kit.TinkerKitUtils;
import com.llamalad7.mixinextras.injector.wrapmethod.WrapMethod;
import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.StructureBlock;
import net.minecraft.block.entity.StructureBlockBlockEntity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.random.Random;
import net.minecraft.world.World;
import net.minecraft.world.tick.TickPriority;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;

@Mixin(value = StructureBlock.class, priority = 1500)
public abstract class StructureBlockMixin extends AbstractBlockImplMixin {

    @Shadow protected abstract void doAction(ServerWorld world, StructureBlockBlockEntity blockEntity);

    @Unique private boolean isPowered = false;

    @WrapMethod(method = "neighborUpdate")
    protected void scheduleOrCall(BlockState state, World world, BlockPos pos, Block sourceBlock, BlockPos sourcePos, boolean notify, Operation<Void> original) {
        if (!(world instanceof ServerWorld))
            return;

        Block block = state.getBlock();

        if (!DoormatTinkerTypes.QC.canModify(block)) {
            original.call(state, world, pos, sourceBlock, sourcePos, notify);
            return;
        }

        int delay = TinkerKitUtils.getDelay(block, 0);
        this.isPowered = TinkerKitUtils.isReceivingRedstonePower(world, pos, block);

        if (delay > 0) {
            TickPriority tickPriority = TinkerKitUtils.getTickPriority(block);
            world.scheduleBlockTick(pos, block, delay, tickPriority);
        }
        else this.getBehaviour(world, pos);
    }

    @Override
    public void scheduledTickImpl(BlockState state, ServerWorld world, BlockPos pos, Random random, Operation<Void> original) {
        this.getBehaviour(world, pos);
        original.call(state, world, pos, random);
    }

    @Unique
    public void getBehaviour(World world, BlockPos pos) {
        if (!(world.getBlockEntity(pos) instanceof StructureBlockBlockEntity blockEntity))
            return;

        boolean bl = blockEntity.isPowered();

        if (this.isPowered == bl)
            return;

        blockEntity.setPowered(this.isPowered);

        if (this.isPowered && world instanceof ServerWorld serverWorld)
            this.doAction(serverWorld, blockEntity);
    }

}
