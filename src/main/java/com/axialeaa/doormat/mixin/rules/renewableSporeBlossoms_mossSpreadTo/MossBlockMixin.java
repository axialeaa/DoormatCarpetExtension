package com.axialeaa.doormat.mixin.rules.renewableSporeBlossoms_mossSpreadTo;

import com.axialeaa.doormat.DoormatSettings;
import com.axialeaa.doormat.world.DoormatConfiguredFeatures;
import com.llamalad7.mixinextras.injector.ModifyReturnValue;
import com.llamalad7.mixinextras.injector.WrapWithCondition;
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
import org.objectweb.asm.Opcodes;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.Slice;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.Optional;
import java.util.function.Consumer;

@Mixin(MossBlock.class)
public class MossBlockMixin {

    /**
     * @return the original "air-above" check for the moss block, with the addition of checking below it as well.
     * @implNote This is so that the two directions can be checked independently of one another in the grow() method.
     */
    @ModifyReturnValue(method = "isFertilizable", at = @At("RETURN"))
    public boolean accommodateBlossoms(boolean original, WorldView world, BlockPos pos) {
        return world.getBlockState(pos.up()).isAir() || world.getBlockState(pos.down()).isAir();
    }

    /**
     * Due to the changes in the above handler method, we need to re-add the "air-above" check to the moss patch feature.
     * @implNote This throws a bunch of errors, but that's just MCDev being silly. It works fine.
     */
    @WrapWithCondition(method = "grow", slice = @Slice(from = @At(value = "FIELD", target = "Lnet/minecraft/world/gen/feature/UndergroundConfiguredFeatures;MOSS_PATCH_BONEMEAL:Lnet/minecraft/registry/RegistryKey;", opcode = Opcodes.GETSTATIC)), at = @At(value = "INVOKE", target = "Ljava/util/Optional;ifPresent(Ljava/util/function/Consumer;)V", ordinal = 0))
    private boolean rewrapMossPatchFeature(Optional<?> optional, Consumer<?> consumer, ServerWorld world, Random random, BlockPos pos, BlockState state) {
        return world.getBlockState(pos.up()).isAir();
    }

    /**
     * Now we can add our custom features with help from the @Unique method further down.
     */
    @Inject(method = "grow", at = @At("HEAD"))
    public void addCustomFeatures(ServerWorld world, Random random, BlockPos pos, BlockState state, CallbackInfo ci) {
        generateAboveOnCondition(DoormatSettings.mossSpreadToCobblestone, DoormatConfiguredFeatures.MOSSY_COBBLESTONE_PATCH, world, random, pos);
        generateAboveOnCondition(DoormatSettings.mossSpreadToStoneBricks, DoormatConfiguredFeatures.MOSSY_STONE_BRICKS_PATCH, world, random, pos);
        if (DoormatSettings.renewableSporeBlossoms && world.getBlockState(pos.down()).isAir())
            world.setBlockState(pos.down(), Blocks.SPORE_BLOSSOM.getDefaultState());
    }

    @Unique
    private void generateAboveOnCondition(boolean rule, RegistryKey<ConfiguredFeature<?, ?>> feature, ServerWorld world, Random random, BlockPos pos) {
        if (rule && world.getBlockState(pos.up()).isAir())
            world.getRegistryManager().getOptional(RegistryKeys.CONFIGURED_FEATURE).flatMap(registry ->
                registry.getEntry(feature)).ifPresent(reference ->
                reference.value().generate(world, world.getChunkManager().getChunkGenerator(), random, pos.up()));
    }

}
