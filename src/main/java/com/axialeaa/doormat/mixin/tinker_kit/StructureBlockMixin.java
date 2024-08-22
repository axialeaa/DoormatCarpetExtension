package com.axialeaa.doormat.mixin.tinker_kit;

import com.axialeaa.doormat.mixin.extensibility.AbstractBlockMixin;
import com.axialeaa.doormat.tinker_kit.TinkerKit;
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
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(StructureBlock.class)
public abstract class StructureBlockMixin extends AbstractBlockMixin {

    @Shadow protected abstract void doAction(ServerWorld world, StructureBlockBlockEntity blockEntity);

    @Unique private boolean isPowered = false;

    @Inject(method = "neighborUpdate", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/World;isReceivingRedstonePower(Lnet/minecraft/util/math/BlockPos;)Z", shift = At.Shift.BEFORE), cancellable = true)
    private void allowQuasiConnectivity(BlockState state, World world, BlockPos pos, Block sourceBlock, BlockPos sourcePos, boolean notify, CallbackInfo ci) {
        Block block = state.getBlock();
        int delay = TinkerKit.getDelay(block, 0);

        this.isPowered = TinkerKit.isReceivingRedstonePower(world, pos, block);

        if (delay > 0) {
            TickPriority tickPriority = TinkerKit.getTickPriority(block);
            world.scheduleBlockTick(pos, block, delay, tickPriority);
        }
        else getBehaviour(world, pos);

        ci.cancel();
    }

    @Override
    public void scheduledTickImpl(BlockState state, ServerWorld world, BlockPos pos, Random random, CallbackInfo ci) {
        getBehaviour(world, pos);
    }

    @Unique
    public void getBehaviour(World world, BlockPos pos) {
        if (!(world.getBlockEntity(pos) instanceof StructureBlockBlockEntity blockEntity))
            return;

        boolean bl = blockEntity.isPowered();

        if (isPowered == bl)
            return;

        blockEntity.setPowered(isPowered);

        if (isPowered && world instanceof ServerWorld serverWorld)
            this.doAction(serverWorld, blockEntity);
    }

}
