package com.axialeaa.doormat.command;

import carpet.utils.CommandHelper;
import carpet.utils.Messenger;
import com.axialeaa.doormat.setting.DoormatSettings;
import com.mojang.brigadier.Command;
import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.arguments.IntegerArgumentType;
import it.unimi.dsi.fastutil.objects.Object2IntArrayMap;
import net.minecraft.block.BlockState;
import net.minecraft.command.argument.BlockPosArgumentType;
import net.minecraft.server.command.ServerCommandSource;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;
import net.minecraft.util.math.random.Random;
import static net.minecraft.server.command.CommandManager.argument;
import static net.minecraft.server.command.CommandManager.literal;

public class RandomTickCommand {

    public static final String ALIAS = "randomtick";
    /**
     * Stores a list of block positions to random tick next game tick, alongside the number of random ticks to send. This is a list and not a single entry for the offchance that two commands are run in the same tick.
     * @see RandomTickCommand#sendRandomTicks(ServerWorld)
     */
    public static final Object2IntArrayMap<BlockPos> SCHEDULED_POSITIONS = new Object2IntArrayMap<>();

    /**
     *<pre> .
     *└──{@link RandomTickCommand#execute(ServerCommandSource) /randomtick}
     *    └──{@link RandomTickCommand#execute(ServerCommandSource, BlockPos) &lt;pos&gt;}
     *        └──{@link RandomTickCommand#execute(ServerCommandSource, BlockPos, int) &lt;count&gt;}</pre>
     */
    public static void register(CommandDispatcher<ServerCommandSource> dispatcher) {
        dispatcher.register(literal(ALIAS)
            .requires(source -> CommandHelper.canUseCommand(source, DoormatSettings.commandRandomTick))
            .executes(ctx -> execute(ctx.getSource()))
            .then(argument("pos", BlockPosArgumentType.blockPos())
                .executes(ctx -> execute(ctx.getSource(),
                    BlockPosArgumentType.getLoadedBlockPos(ctx, "pos"))
                )
                .then(argument("count", IntegerArgumentType.integer(1, 1024))
                    .executes(ctx -> execute(ctx.getSource(),
                        BlockPosArgumentType.getLoadedBlockPos(ctx, "pos"),
                        IntegerArgumentType.getInteger(ctx, "count"))
                    )
                )
            )
        );
    }

    /**
     * Sends {@code count} random tick(s) to {@code pos}.
     */
    private static int execute(ServerCommandSource source, BlockPos pos, int count) {
        ServerWorld world = source.getWorld();
        BlockState blockState = world.getBlockState(pos);

        if (!blockState.hasRandomTicks()) {
            Messenger.m(source, "r The block at ", Messenger.tp("c", pos), "r  does not support random ticks!");
            return 0;
        }

        if (SCHEDULED_POSITIONS.containsKey(pos)) {
            Messenger.m(source, "r Already sent random tick(s) to the block at ", Messenger.tp("c", pos), "r  in this tick!");
            return 0;
        }

        SCHEDULED_POSITIONS.put(pos, count);
        Messenger.m(source, "w Sent %s random tick(s) to the block at ".formatted(count), Messenger.tp("c", pos));

        return Command.SINGLE_SUCCESS;
    }

    /**
     * Sends 1 random tick to {@code pos}.
     */
    private static int execute(ServerCommandSource source, BlockPos pos) {
        return execute(source, pos, 1);
    }

    /**
     * Sends 1 random tick to the block position of {@code source}.
     */
    private static int execute(ServerCommandSource source) {
        Vec3d vec3d = source.getPosition();
        BlockPos pos = BlockPos.ofFloored(vec3d);

        return execute(source, pos);
    }

    public static void sendRandomTicks(ServerWorld world) {
        if (SCHEDULED_POSITIONS.isEmpty())
            return;

        Random random = world.getRandom();

        for (BlockPos pos : SCHEDULED_POSITIONS.keySet()) {
            int count = SCHEDULED_POSITIONS.getInt(pos);

            sendRandomTick(world, pos, count, random);
            SCHEDULED_POSITIONS.remove(pos, count);
        }
    }

    private static void sendRandomTick(ServerWorld world, BlockPos pos, int count, Random random) {
        BlockState blockState = world.getBlockState(pos);

        for (int i = 0; i < count; i++) {
            if (!blockState.hasRandomTicks()) // If the block at this position no longer supports random ticks (it grows), break the loop
                return;

            blockState.randomTick(world, pos, random);
        }
    }

}
