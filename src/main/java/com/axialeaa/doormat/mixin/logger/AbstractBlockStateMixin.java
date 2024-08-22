package com.axialeaa.doormat.mixin.logger;

import carpet.logging.LoggerRegistry;
import com.axialeaa.doormat.DoormatLoggers;
import com.axialeaa.doormat.tinker_kit.TinkerKit;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.block.AbstractBlock;
import net.minecraft.block.Block;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.text.Text;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.ChunkPos;
import net.minecraft.util.math.random.Random;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.ArrayList;
import java.util.List;

import static carpet.utils.Messenger.c;
import static carpet.utils.Messenger.tp;

@Environment(EnvType.CLIENT)
@Mixin(AbstractBlock.AbstractBlockState.class)
public abstract class AbstractBlockStateMixin {

    @Shadow public abstract Block getBlock();

    @Unique private static long prevTime = 0;
    @Unique private static int count = 0;
    @Unique private static boolean newTick;

    @Inject(method = "randomTick", at = @At("HEAD"))
    private void printRandomTickLogLine(ServerWorld world, BlockPos pos, Random random, CallbackInfo ci) {
        if (!DoormatLoggers.__randomTicks)
            return;

        LoggerRegistry.getLogger("randomTicks").log(option -> {
            long time = world.getTime();

            newTick = false;

            if (prevTime != time) {
                count = 0;
                prevTime = time;
                newTick = true;
            }

            count++;
            List<Text> messages = new ArrayList<>();

            if (newTick)
                messages.add(c("wb tick : ", "d " + time));

            Block block = this.getBlock();
            String key = TinkerKit.getKey(block);

            if ("brief".equals(option))
                messages.add(c("d #" + count, "gb ->", tp("l", pos), "m (" + key + ")"));
            else if ("full".equals(option)) {
                messages.add(c("d #" + count, "gb ->", tp("l", pos)));

                ChunkPos chunkPos = world.getChunk(pos).getPos();
                String dimension = world.getDimensionEntry().getIdAsString();

                messages.add(c("w   block: ", "m " + key));
                messages.add(c("w   chunk: ", "c " + chunkPos));
                messages.add(c("w   dimension: ", "m " + dimension));
            }

            return messages.toArray(new Text[0]);
        });
    }

}
