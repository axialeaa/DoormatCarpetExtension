package com.axialeaa.doormat.mixin.rule.renewableSporeBlossoms_mossSpreadTo;

import com.axialeaa.doormat.DoormatSettings;
import com.axialeaa.doormat.world.DoormatConfiguredFeatures;
import com.llamalad7.mixinextras.injector.ModifyReturnValue;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.block.MossBlock;
import net.minecraft.registry.RegistryKey;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.random.Random;
import net.minecraft.world.WorldView;
import net.minecraft.world.gen.feature.ConfiguredFeature;
import net.minecraft.world.gen.feature.UndergroundConfiguredFeatures;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(MossBlock.class)
public class MossBlockMixin {

    @Unique
    private void generateAboveOnCondition(boolean condition, RegistryKey<ConfiguredFeature<?, ?>> feature, ServerWorld world, Random random, BlockPos pos) {
        if (condition && world.getBlockState(pos.up()).isAir())
            world.getRegistryManager().getOptional(RegistryKeys.CONFIGURED_FEATURE).flatMap(registry ->
                registry.getEntry(feature)).ifPresent(reference ->
                reference.value().generate(world, world.getChunkManager().getChunkGenerator(), random, pos));
    }

    @ModifyReturnValue(method = "isFertilizable", at = @At("RETURN"))
    public boolean accommodateBlossoms(boolean original, WorldView world, BlockPos pos) {
        return original || world.getBlockState(pos.down()).isAir();
        // this just allows us to check for air independently above and below in the grow class
        // no vanilla behaviour changed here
    }

    @Inject(method = "grow", at = @At("HEAD"), cancellable = true)
    public void overwriteWithCustom(ServerWorld world, Random random, BlockPos pos, BlockState state, CallbackInfo ci) {
        ci.cancel();
        generateAboveOnCondition(true, UndergroundConfiguredFeatures.MOSS_PATCH_BONEMEAL, world, random, pos);
        generateAboveOnCondition(DoormatSettings.mossSpreadToCobblestone, DoormatConfiguredFeatures.MOSSY_COBBLESTONE_PATCH, world, random, pos);
        generateAboveOnCondition(DoormatSettings.mossSpreadToStoneBricks, DoormatConfiguredFeatures.MOSSY_STONE_BRICKS_PATCH, world, random, pos);
        if (DoormatSettings.renewableSporeBlossoms == DoormatSettings.RenewableSporeBlossomsMode.MOSS && world.getBlockState(pos.down()).isAir())
            world.setBlockState(pos.down(), Blocks.SPORE_BLOSSOM.getDefaultState());
    }

}
