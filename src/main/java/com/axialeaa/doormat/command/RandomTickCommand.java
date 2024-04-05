package com.axialeaa.doormat.command;

import carpet.utils.CommandHelper;
import carpet.utils.Messenger;
import com.axialeaa.doormat.DoormatSettings;
import com.mojang.brigadier.Command;
import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.arguments.IntegerArgumentType;
import net.minecraft.block.BlockState;
import net.minecraft.command.argument.BlockPosArgumentType;
import net.minecraft.server.command.ServerCommandSource;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.math.BlockPos;

import static net.minecraft.server.command.CommandManager.argument;
import static net.minecraft.server.command.CommandManager.literal;

/**
 * <code>/randomtick</code> - Sends 1 random tick to the block position the command is called from.<br>
 * <code>/randomtick &lt;pos&gt;</code> - Sends 1 random tick to the specified block position.<br>
 * <code>/randomtick &lt;pos&gt; &lt;count&gt;</code> - Sends <code>count</code> random ticks to the specified block position.
 */
public class RandomTickCommand {

    public static final String ALIAS = "randomtick";

    public static void register(CommandDispatcher<ServerCommandSource> dispatcher) {
        dispatcher.register(literal(ALIAS)
            .requires(player -> CommandHelper.canUseCommand(player, DoormatSettings.commandRandomTick))
            .executes(ctx -> execute(
                ctx.getSource(),
                BlockPos.ofFloored(ctx.getSource().getPosition()),
                1
            ))
            .then(argument("pos", BlockPosArgumentType.blockPos())
                .executes(ctx -> execute(
                    ctx.getSource(),
                    BlockPosArgumentType.getLoadedBlockPos(ctx, "pos"),
                    1
                ))
                .then(argument("count", IntegerArgumentType.integer(1, 4096))
                    .executes(ctx -> execute(
                        ctx.getSource(),
                        BlockPosArgumentType.getLoadedBlockPos(ctx, "pos"),
                        IntegerArgumentType.getInteger(ctx, "count")
                    ))
                )
            )
        );
    }

    private static int execute(ServerCommandSource source, BlockPos pos, int count) {
        ServerWorld world = source.getWorld();
        BlockState state = world.getBlockState(pos);

        if (state.hasRandomTicks()) {
            for (int i = 0; i < count; i++)
                state.randomTick(world, pos, world.getRandom());

            Messenger.m(source, "w Sent " + count + " random tick(s) to the block at " + pos.getX() + ", " + pos.getY() + ", " + pos.getZ());
            return Command.SINGLE_SUCCESS;
        }
        else {
            Messenger.m(source, "r Could not random tick the block");
            return 0;
        }
    }

}
