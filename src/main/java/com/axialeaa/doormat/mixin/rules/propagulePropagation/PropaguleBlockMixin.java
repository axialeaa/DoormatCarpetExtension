package com.axialeaa.doormat.mixin.rules.propagulePropagation;

import com.axialeaa.doormat.DoormatSettings;
import net.minecraft.block.BlockState;
import net.minecraft.block.PropaguleBlock;
import net.minecraft.entity.FallingBlockEntity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.state.property.IntProperty;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.util.math.random.Random;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(PropaguleBlock.class)
public class PropaguleBlockMixin {

    @Shadow @Final public static IntProperty AGE;

    /**
     * Creates a falling block entity of a fully-grown, hanging propagule state next randomTick.
     */
    @Inject(method = "randomTick", at = @At(value = "INVOKE", target = "Lnet/minecraft/block/PropaguleBlock;isFullyGrown(Lnet/minecraft/block/BlockState;)Z"))
    private void dropOnFullyGrown(BlockState state, ServerWorld world, BlockPos pos, Random random, CallbackInfo ci) {
        if (DoormatSettings.propagulePropagation && state.get(AGE) == 4)
            spawnFallingBlock(state, world, pos);
    }

    @Unique
    private static void spawnFallingBlock(BlockState state, ServerWorld world, BlockPos pos) {
        BlockPos.Mutable mutable = pos.mutableCopy();
        FallingBlockEntity.spawnFromBlock(world, mutable, state);
        mutable.move(Direction.DOWN);
    }

}