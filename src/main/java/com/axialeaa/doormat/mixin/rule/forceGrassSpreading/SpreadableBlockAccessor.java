package com.axialeaa.doormat.mixin.rule.forceGrassSpreading;

import net.minecraft.block.BlockState;
import net.minecraft.block.SpreadableBlock;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.WorldView;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Invoker;

@Mixin(SpreadableBlock.class)
public interface SpreadableBlockAccessor {

    @Invoker("canSpread")
    static boolean invokeCanSpread(BlockState state, WorldView world, BlockPos pos) {
        throw new AssertionError();
    }

}
