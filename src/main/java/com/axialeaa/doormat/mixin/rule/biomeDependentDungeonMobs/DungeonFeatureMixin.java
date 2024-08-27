package com.axialeaa.doormat.mixin.rule.biomeDependentDungeonMobs;

import com.axialeaa.doormat.settings.DoormatSettings;
import com.axialeaa.doormat.helper.DungeonBiomeSpawningHelper;
import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import com.llamalad7.mixinextras.sugar.Local;
import net.minecraft.entity.EntityType;
import net.minecraft.registry.entry.RegistryEntry;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.random.Random;
import net.minecraft.world.StructureWorldAccess;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.gen.feature.DungeonFeature;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

@Mixin(DungeonFeature.class)
public class DungeonFeatureMixin {

    @WrapOperation(method = "generate", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/gen/feature/DungeonFeature;getMobSpawnerEntity(Lnet/minecraft/util/math/random/Random;)Lnet/minecraft/entity/EntityType;"))
    private EntityType<?> modifySpawnerEntity(DungeonFeature instance, Random random, Operation<EntityType<?>> original, @Local StructureWorldAccess structureWorldAccess, @Local(ordinal = 0) BlockPos blockPos) {
        EntityType<?> type = original.call(instance, random);

        if (!DoormatSettings.biomeDependentDungeonMobs)
            return type;

        RegistryEntry<Biome> registryEntry = structureWorldAccess.getBiome(blockPos);
        Biome biome = registryEntry.value();

        return DungeonBiomeSpawningHelper.getAlternativeForBiome(type, biome).orElse(type);
    }

}
