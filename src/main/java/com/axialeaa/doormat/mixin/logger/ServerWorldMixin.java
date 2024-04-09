package com.axialeaa.doormat.mixin.logger;

import carpet.logging.LoggerRegistry;
import com.axialeaa.doormat.DoormatLoggers;
import com.axialeaa.doormat.util.RenderHandler;
import com.llamalad7.mixinextras.sugar.Local;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.block.BlockState;
import net.minecraft.fluid.FluidState;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.chunk.WorldChunk;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.awt.*;
import java.util.Objects;

@Environment(EnvType.CLIENT)
@Mixin(ServerWorld.class)
public class ServerWorldMixin {

    @Inject(method = "tickChunk", at = @At(value = "INVOKE", target = "Lnet/minecraft/util/profiler/Profiler;pop()V", ordinal = 0, shift = At.Shift.BEFORE))
    private void renderRandomTickLog(WorldChunk chunk, int randomTickSpeed, CallbackInfo ci, @Local BlockPos blockPos2, @Local BlockState blockState, @Local FluidState fluidState) {
        if (DoormatLoggers.__randomTicks) {
            LoggerRegistry.getLogger("randomTicks").log(option -> {
                if (!Objects.equals(option, "chat")) {
                    Color color = RenderHandler.getTrubetskoyColor("red", 255);

                    if (blockState.hasRandomTicks() || fluidState.hasRandomTicks()) {
                        color = RenderHandler.getTrubetskoyColor("green", 255);
                        // Render residual "ticked prior" box
                        RenderHandler.addCuboidLines(blockPos2.toCenterPos(), 0.5, color, 40, true);
                    }

                    // Render randomTick execution position
                    RenderHandler.addCuboidQuads(blockPos2.toCenterPos(), 0.5, RenderHandler.getColorWithAlpha(color, 40), 1, true);
                }
                return null;
            });
        }
    }

}
