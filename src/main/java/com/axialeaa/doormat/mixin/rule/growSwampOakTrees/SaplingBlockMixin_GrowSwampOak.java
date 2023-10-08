package com.axialeaa.doormat.mixin.rule.growSwampOakTrees;

import com.axialeaa.doormat.DoormatSettings;
import com.axialeaa.doormat.block.SwampOakSaplingGenerator;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.block.SaplingBlock;
import net.minecraft.block.sapling.SaplingGenerator;
import net.minecraft.registry.tag.BiomeTags;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.random.Random;
import net.minecraft.world.gen.chunk.ChunkGenerator;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

@Mixin(SaplingBlock.class)
public class SaplingBlockMixin_GrowSwampOak {

    @Unique private static final SwampOakSaplingGenerator SWAMP_OAK_GENERATOR = new SwampOakSaplingGenerator();

    @Redirect(method = "generate", at = @At(value = "INVOKE", target = "Lnet/minecraft/block/sapling/SaplingGenerator;generate(Lnet/minecraft/server/world/ServerWorld;Lnet/minecraft/world/gen/chunk/ChunkGenerator;Lnet/minecraft/util/math/BlockPos;Lnet/minecraft/block/BlockState;Lnet/minecraft/util/math/random/Random;)Z"))
    protected boolean swampGenerate(SaplingGenerator generator, ServerWorld world, ChunkGenerator chunkGenerator, BlockPos pos, BlockState state, Random random) {
        if (DoormatSettings.growSwampOakTrees && world.getBiome(pos).isIn(BiomeTags.ALLOWS_SURFACE_SLIME_SPAWNS) && state.isOf(Blocks.OAK_SAPLING))
            SWAMP_OAK_GENERATOR.generate(world, world.getChunkManager().getChunkGenerator(), pos, state, random);
        return generator.generate(world, world.getChunkManager().getChunkGenerator(), pos, state, random);
    }

}
