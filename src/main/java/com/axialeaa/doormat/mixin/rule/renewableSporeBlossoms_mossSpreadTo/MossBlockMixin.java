package com.axialeaa.doormat.mixin.rule.renewableSporeBlossoms_mossSpreadTo;

import com.axialeaa.doormat.DoormatSettings;
import com.axialeaa.doormat.world.DoormatConfiguredFeatures;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.block.MossBlock;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.random.Random;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(MossBlock.class)
public class MossBlockMixin {

    @Inject(method = "grow", at = @At("HEAD"))
    private void grow(ServerWorld world, Random random, BlockPos pos, BlockState state, CallbackInfo ci) {
        if (DoormatSettings.renewableSporeBlossoms == DoormatSettings.RenewableSporeBlossomsMode.MOSS && world.getBlockState(pos.down()).isAir())
            world.setBlockState(pos.down(), Blocks.SPORE_BLOSSOM.getDefaultState());
        if (DoormatSettings.mossSpreadToCobblestone)
            world.getRegistryManager().getOptional(RegistryKeys.CONFIGURED_FEATURE).flatMap(registry ->
                    registry.getEntry(DoormatConfiguredFeatures.MOSSY_COBBLESTONE_PATCH)).ifPresent(reference ->
                    reference.value().generate(world, world.getChunkManager().getChunkGenerator(), random, pos.up())
            );
        if (DoormatSettings.mossSpreadToStoneBricks)
            world.getRegistryManager().getOptional(RegistryKeys.CONFIGURED_FEATURE).flatMap(registry ->
                    registry.getEntry(DoormatConfiguredFeatures.MOSSY_STONE_BRICKS_PATCH)).ifPresent(reference ->
                    reference.value().generate(world, world.getChunkManager().getChunkGenerator(), random, pos.up())
            );
    }

}
