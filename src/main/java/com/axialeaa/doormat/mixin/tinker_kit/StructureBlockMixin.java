package com.axialeaa.doormat.mixin.tinker_kit;

import com.axialeaa.doormat.fake.TinkerKitBehaviourSetter;
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
public abstract class StructureBlockMixin extends AbstractBlockMixin implements TinkerKitBehaviourSetter {

    @Shadow protected abstract void doAction(ServerWorld world, StructureBlockBlockEntity blockEntity);

    @Unique private boolean isPowered = false;

    @Inject(method = "neighborUpdate", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/World;isReceivingRedstonePower(Lnet/minecraft/util/math/BlockPos;)Z", shift = At.Shift.BEFORE), cancellable = true)
    private void allowQuasiConnectivity(BlockState state, World world, BlockPos pos, Block sourceBlock, BlockPos sourcePos, boolean notify, CallbackInfo ci) {
        int delay = TinkerKit.getDelay(state, 0);
        this.isPowered = TinkerKit.isReceivingRedstonePower(world, pos, state);

        if (delay == 0)
            getBehaviour(world, pos, state);
        else world.scheduleBlockTick(pos, state.getBlock(), delay, TinkerKit.getTickPriority(state, TickPriority.NORMAL));

        ci.cancel();
    }

    @Override
    public void scheduledTickImpl(BlockState state, ServerWorld world, BlockPos pos, Random random, CallbackInfo ci) {
        getBehaviour(world, pos, state);
    }

    @Override
    public void getBehaviour(World world, BlockPos pos, BlockState state) {
        if (world.getBlockEntity(pos) instanceof StructureBlockBlockEntity blockEntity) {
            boolean bl = blockEntity.isPowered();

            if (isPowered && !bl) {
                blockEntity.setPowered(true);
                this.doAction((ServerWorld) world, blockEntity);
            }
            else if (!isPowered && bl)
                blockEntity.setPowered(false);
        }
    }

}
