package com.axialeaa.doormat.mixin.logger;

import com.llamalad7.mixinextras.sugar.Local;
import net.minecraft.block.BlockState;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.chunk.WorldChunk;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(ServerWorld.class)
public abstract class ServerWorldMixin {

    @Inject(method = "tickChunk", at = @At(value = "INVOKE", target = "Lnet/minecraft/util/profiler/Profiler;pop()V", ordinal = 0, shift = At.Shift.BEFORE))
    private void renderRandomTickLog(WorldChunk chunk, int randomTickSpeed, CallbackInfo ci, @Local BlockPos blockPos2, @Local BlockState blockState) {
//        if (DoormatLoggers.__randomTicks) {
//            LoggerRegistry.getLogger("randomTicks").log(option -> {
//                if (!Objects.equals(option, "chat")) {
//                    Color color = RenderHandler.getTrubetskoyColor("red");
//
//                    if (blockState.hasRandomTicks() || fluidState.hasRandomTicks()) {
//                        color = RenderHandler.getTrubetskoyColor("green");
//                        // Render residual "ticked prior" box
//                        RenderHandler.addCuboidLines(blockPos2.toCenterPos(), 0.5, color, 40, true);
//                    }
//
//                    // Render randomTick execution position
//                    RenderHandler.addCuboidQuads(blockPos2.toCenterPos(), 0.5, RenderHandler.getColorWithAlpha(color, 40), 1, true);
//                }
//                return null;
//            });
//        }
    }

}
