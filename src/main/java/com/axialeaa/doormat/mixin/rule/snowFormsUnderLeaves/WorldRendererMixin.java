package com.axialeaa.doormat.mixin.rule.snowFormsUnderLeaves;

import com.axialeaa.doormat.DoormatSettings;
import com.llamalad7.mixinextras.sugar.Local;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.render.WorldRenderer;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.Heightmap;
import net.minecraft.world.biome.Biome;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyArg;

@Environment(EnvType.CLIENT)
@Mixin(WorldRenderer.class)
public class WorldRendererMixin {

    @ModifyArg(method = "renderWeather", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/World;getTopY(Lnet/minecraft/world/Heightmap$Type;II)I"))
    private Heightmap.Type modifyRenderHeightmap(Heightmap.Type heightmap, @Local Biome biome, @Local BlockPos.Mutable mutable) {
        if (DoormatSettings.snowFormsUnderLeaves && biome.getPrecipitation(mutable) == Biome.Precipitation.SNOW)
            return Heightmap.Type.MOTION_BLOCKING_NO_LEAVES;
        return heightmap;
    }

}
