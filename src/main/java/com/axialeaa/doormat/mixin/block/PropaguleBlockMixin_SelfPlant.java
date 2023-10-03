package com.axialeaa.doormat.mixin.block;

import com.axialeaa.doormat.DoormatSettings;
import com.axialeaa.doormat.MainEntrypoint;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.block.LandingBlock;
import net.minecraft.block.PropaguleBlock;
import net.minecraft.entity.FallingBlockEntity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.state.property.IntProperty;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.util.math.random.Random;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.*;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Debug(export = true)
@Mixin(PropaguleBlock.class)
public class PropaguleBlockMixin_SelfPlant implements LandingBlock {

    @Shadow @Final public static IntProperty AGE;

    @Inject(method = "randomTick", at = @At(value = "INVOKE", target = "Lnet/minecraft/block/PropaguleBlock;isFullyGrown(Lnet/minecraft/block/BlockState;)Z"))
    private void dropOnFullyGrown(BlockState state, ServerWorld world, BlockPos pos, Random random, CallbackInfo ci) {
        if (DoormatSettings.propagulePropagation && state.get(AGE) == 4) spawnFallingBlock(state, world, pos);
    }

    @Unique
    private static void spawnFallingBlock(BlockState state, ServerWorld world, BlockPos pos) {
        BlockPos.Mutable mutable = pos.mutableCopy();
        FallingBlockEntity.spawnFromBlock(world, mutable, state);
        mutable.move(Direction.DOWN);
    }

    public void onLanding(World world, BlockPos pos, BlockState fallingBlockState, BlockState currentStateInPos, FallingBlockEntity fallingBlockEntity) {
        world.setBlockState(pos, Blocks.MANGROVE_PROPAGULE.getDefaultState(), 3);
        MainEntrypoint.LOGGER.info("placed");
    }

}
