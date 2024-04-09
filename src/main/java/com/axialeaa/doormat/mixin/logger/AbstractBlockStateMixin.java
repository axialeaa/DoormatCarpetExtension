package com.axialeaa.doormat.mixin.logger;

import carpet.logging.LoggerRegistry;
import carpet.utils.Messenger;
import com.axialeaa.doormat.DoormatLoggers;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.block.AbstractBlock;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.text.Text;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.random.Random;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.Objects;

@Environment(EnvType.CLIENT)
@Mixin(AbstractBlock.AbstractBlockState.class)
public class AbstractBlockStateMixin {

    @Inject(method = "randomTick", at = @At("HEAD"))
    private void printRandomTickLogLine(ServerWorld world, BlockPos pos, Random random, CallbackInfo ci) {
        if (DoormatLoggers.__randomTicks) {
            LoggerRegistry.getLogger("randomTicks").log(option ->
                Objects.equals(option, "visual") && world.getBlockState(pos).hasRandomTicks() ? null :
                    new Text[]{ Messenger.c("w Successful randomTick at ", Messenger.tp("c", pos)) }
            );
        }
    }

}
