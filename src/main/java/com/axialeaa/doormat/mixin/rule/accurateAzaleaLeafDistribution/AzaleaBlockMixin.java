package com.axialeaa.doormat.mixin.rule.accurateAzaleaLeafDistribution;

import com.axialeaa.doormat.block.DoormatSaplingGenerator;
import com.axialeaa.doormat.DoormatSettings;
import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import net.minecraft.block.*;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.random.Random;
import net.minecraft.world.gen.chunk.ChunkGenerator;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

@Mixin(AzaleaBlock.class)
public class AzaleaBlockMixin {

    /**
     * @return an azalea tree with the matching quantity of flowers if the rule is enabled, otherwise a normal azalea tree.
     */
    @WrapOperation(method = "grow", at = @At(value = "INVOKE", target = "Lnet/minecraft/block/SaplingGenerator;generate(Lnet/minecraft/server/world/ServerWorld;Lnet/minecraft/world/gen/chunk/ChunkGenerator;Lnet/minecraft/util/math/BlockPos;Lnet/minecraft/block/BlockState;Lnet/minecraft/util/math/random/Random;)Z"))
    public boolean generateAccurateTree(SaplingGenerator instance, ServerWorld world, ChunkGenerator chunkGenerator, BlockPos pos, BlockState state, Random random, Operation<Boolean> original) {
        if (DoormatSettings.accurateAzaleaLeafDistribution) {
            Block block = state.getBlock();
            if (block == Blocks.AZALEA)
                return DoormatSaplingGenerator.AZALEA_NO_FLOWERS.generate(world, chunkGenerator, pos, state, random);
            else if (block == Blocks.FLOWERING_AZALEA)
                return DoormatSaplingGenerator.AZALEA_MANY_FLOWERS.generate(world, chunkGenerator, pos, state, random);
        }
        return original.call(instance, world, chunkGenerator, pos, state, random);
    }

}