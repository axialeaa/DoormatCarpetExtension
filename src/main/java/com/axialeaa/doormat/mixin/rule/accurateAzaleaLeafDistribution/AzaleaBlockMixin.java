package com.axialeaa.doormat.mixin.rule.accurateAzaleaLeafDistribution;

import com.axialeaa.doormat.DoormatSettings;
import com.axialeaa.doormat.block.AzaleaSaplingManyFlowersGenerator;
import com.axialeaa.doormat.block.AzaleaSaplingNoFlowersGenerator;
import net.minecraft.block.AzaleaBlock;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.block.sapling.AzaleaSaplingGenerator;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.random.Random;
import net.minecraft.world.gen.chunk.ChunkGenerator;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

@Mixin(AzaleaBlock.class)
public class AzaleaBlockMixin {

    @Unique private static final AzaleaSaplingManyFlowersGenerator MANY_FLOWERS_GENERATOR = new AzaleaSaplingManyFlowersGenerator();
    @Unique private static final AzaleaSaplingNoFlowersGenerator NO_FLOWERS_GENERATOR = new AzaleaSaplingNoFlowersGenerator();

    @Redirect(method = "grow", at = @At(value = "INVOKE", target = "Lnet/minecraft/block/sapling/AzaleaSaplingGenerator;generate(Lnet/minecraft/server/world/ServerWorld;Lnet/minecraft/world/gen/chunk/ChunkGenerator;Lnet/minecraft/util/math/BlockPos;Lnet/minecraft/block/BlockState;Lnet/minecraft/util/math/random/Random;)Z"))
    public boolean separateTrees(AzaleaSaplingGenerator generator, ServerWorld world, ChunkGenerator chunkGenerator, BlockPos pos, BlockState state, Random random) {
        return DoormatSettings.accurateAzaleaLeafDistribution ?
            // if the rule is enabled...
            state.getBlock() == Blocks.FLOWERING_AZALEA ?
                // if the fertilized block is a flowering azalea...
                MANY_FLOWERS_GENERATOR.generate(world, chunkGenerator, pos, state, random) :
                NO_FLOWERS_GENERATOR.generate(world, chunkGenerator, pos, state, random) :
                // create a tree with many flowers, otherwise a tree with no flowers
            generator.generate(world, chunkGenerator, pos, state, random); // otherwise generate a vanilla azalea tree
    }

}