package com.axialeaa.doormat.mixin.rule.growableSwampOakTrees;

import com.axialeaa.doormat.block.DoormatSaplingGenerator;
import com.axialeaa.doormat.DoormatSettings;
import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.block.SaplingBlock;
import net.minecraft.block.SaplingGenerator;
import net.minecraft.registry.tag.BiomeTags;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.random.Random;
import net.minecraft.world.gen.chunk.ChunkGenerator;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

@Mixin(SaplingBlock.class)
public class SaplingBlockMixin {

    /**
     * @return a swamp oak tree if the rule is enabled, the sapling is oak and the biome is a swamp, otherwise a normal tree.
     */
    @WrapOperation(method = "generate", at = @At(value = "INVOKE", target = "Lnet/minecraft/block/SaplingGenerator;generate(Lnet/minecraft/server/world/ServerWorld;Lnet/minecraft/world/gen/chunk/ChunkGenerator;Lnet/minecraft/util/math/BlockPos;Lnet/minecraft/block/BlockState;Lnet/minecraft/util/math/random/Random;)Z"))
    protected boolean generateSwampOak(SaplingGenerator instance, ServerWorld world, ChunkGenerator chunkGenerator, BlockPos pos, BlockState state, Random random, Operation<Boolean> original) {
        return DoormatSettings.growableSwampOakTrees && state.isOf(Blocks.OAK_SAPLING) && world.getBiome(pos).isIn(BiomeTags.ALLOWS_SURFACE_SLIME_SPAWNS) ?
            DoormatSaplingGenerator.SWAMP_OAK.generate(world, chunkGenerator, pos, state, random) :
            original.call(instance, world, chunkGenerator, pos, state, random);
    }

}
