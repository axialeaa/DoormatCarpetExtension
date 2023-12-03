package com.axialeaa.doormat.mixin.rules.fastLeafDecay;

import com.axialeaa.doormat.DoormatSettings;
import net.minecraft.block.BlockState;
import net.minecraft.block.LeavesBlock;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.random.Random;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(LeavesBlock.class)
public abstract class LeavesBlockMixin {

    @Shadow protected abstract boolean shouldDecay(BlockState state);

    /**
     * If the rule is enabled and the leaf block receives a scheduled tick, try to decay it with a random chance. If it fails, schedule another tick until the block is removed.
     */
    @Inject(method = "scheduledTick", at = @At("TAIL"))
    private void decayOnScheduledTick(BlockState state, ServerWorld world, BlockPos pos, Random random, CallbackInfo ci) {
        if (DoormatSettings.fastLeafDecay > 0.0)
            if (shouldDecay(state) && random.nextFloat() < DoormatSettings.fastLeafDecay) {
                LeavesBlock.dropStacks(state, world, pos);
                world.removeBlock(pos, false);
            }
            else world.scheduleBlockTick(pos, state.getBlock(), 1);
    }

}
