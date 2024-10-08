package com.axialeaa.doormat.mixin.logger;

import carpet.logging.LoggerRegistry;
import com.axialeaa.doormat.registry.DoormatLoggers;
import com.axialeaa.doormat.tinker_kit.TinkerKitUtils;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.block.AbstractBlock;
import net.minecraft.block.Block;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.text.Text;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.ChunkPos;
import net.minecraft.util.math.random.Random;
import net.minecraft.world.World;
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

    @Inject(method = "randomTick", at = @At("HEAD"))
    private void printRandomTickLogLine(ServerWorld world, BlockPos pos, Random random, CallbackInfo ci) {
        if (!DoormatLoggers.randomTicks)
            return;

        LoggerRegistry.getLogger("randomTicks").log(option -> this.getMessagesArray(world, pos, option));
    }

    @Unique
    private Text[] getMessagesArray(World world, BlockPos pos, String option) {
        long time = world.getTime();

        boolean newTick = false;

        if (prevTime != time) {
            count = 0;
            prevTime = time;
            newTick = true;
        }

        count++;
        List<Text> messages = new ArrayList<>();

        if (newTick)
            messages.add(c("wb tick : ", "d %d".formatted(time)));

        Block block = this.getBlock();
        String key = TinkerKitUtils.getKey(block);

        if ("brief".equals(option))
            messages.add(c("d #%d".formatted(count), "gb ->", tp("l", pos), "m (%s)".formatted(key)));
        else if ("full".equals(option)) {
            messages.add(c("d #%d".formatted(count), "gb ->", tp("l", pos)));

            ChunkPos chunkPos = world.getChunk(pos).getPos();
            String dimension = world.getDimensionEntry().getIdAsString();

            messages.add(c("w   block: ", "m %s".formatted(key)));
            messages.add(c("w   chunk: ", "c %s".formatted(chunkPos)));
            messages.add(c("w   dimension: ", "m %s".formatted(dimension)));
        }

        return messages.toArray(new Text[0]);
    }

}
