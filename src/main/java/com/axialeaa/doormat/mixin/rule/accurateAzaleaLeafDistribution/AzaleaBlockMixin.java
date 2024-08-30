package com.axialeaa.doormat.mixin.rule.accurateAzaleaLeafDistribution;

import com.axialeaa.doormat.registry.DoormatSaplingGenerators;
import com.axialeaa.doormat.setting.DoormatSettings;
import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import net.minecraft.block.AzaleaBlock;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.SaplingGenerator;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.random.Random;
import net.minecraft.world.gen.chunk.ChunkGenerator;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

@Mixin(AzaleaBlock.class)
public class AzaleaBlockMixin {

    @WrapOperation(method = "grow", at = @At(value = "INVOKE", target = "Lnet/minecraft/block/SaplingGenerator;generate(Lnet/minecraft/server/world/ServerWorld;Lnet/minecraft/world/gen/chunk/ChunkGenerator;Lnet/minecraft/util/math/BlockPos;Lnet/minecraft/block/BlockState;Lnet/minecraft/util/math/random/Random;)Z"))
    public boolean generateAccurateTree(SaplingGenerator instance, ServerWorld world, ChunkGenerator chunkGenerator, BlockPos pos, BlockState state, Random random, Operation<Boolean> original) {
        if (!DoormatSettings.accurateAzaleaLeafDistribution)
            return original.call(instance, world, chunkGenerator, pos, state, random);

        Block block = state.getBlock();

        if (!DoormatSaplingGenerators.AZALEA_MAP.containsKey(block))
            return original.call(instance, world, chunkGenerator, pos, state, random);

        return DoormatSaplingGenerators.AZALEA_MAP.get(block).generate(world, chunkGenerator, pos, state, random);
    }

}