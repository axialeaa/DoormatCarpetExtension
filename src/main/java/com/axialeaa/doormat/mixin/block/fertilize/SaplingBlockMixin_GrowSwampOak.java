package com.axialeaa.doormat.mixin.block.fertilize;

import com.axialeaa.doormat.DoormatSettings;
import com.axialeaa.doormat.block.SwampOakSaplingGenerator;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.block.SaplingBlock;
import net.minecraft.registry.tag.BiomeTags;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.random.Random;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(SaplingBlock.class)
public class SaplingBlockMixin_GrowSwampOak {

    @Unique private static final SwampOakSaplingGenerator SWAMP_OAK_GENERATOR = new SwampOakSaplingGenerator();

    @Inject(method = "generate", at = @At(value = "INVOKE", target = "Lnet/minecraft/block/sapling/SaplingGenerator;generate(Lnet/minecraft/server/world/ServerWorld;Lnet/minecraft/world/gen/chunk/ChunkGenerator;Lnet/minecraft/util/math/BlockPos;Lnet/minecraft/block/BlockState;Lnet/minecraft/util/math/random/Random;)Z"), cancellable = true)
    protected void swampGenerate(ServerWorld world, BlockPos pos, BlockState state, Random random, CallbackInfo ci) {
        if (DoormatSettings.growSwampOakTrees && world.getBiome(pos).isIn(BiomeTags.ALLOWS_SURFACE_SLIME_SPAWNS) && state.isOf(Blocks.OAK_SAPLING)) {
            ci.cancel();
            SWAMP_OAK_GENERATOR.generate(world, world.getChunkManager().getChunkGenerator(), pos, state, random);
        }
    }

}
