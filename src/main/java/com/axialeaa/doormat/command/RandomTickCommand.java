package com.axialeaa.doormat.command;

import carpet.utils.CommandHelper;
import com.axialeaa.doormat.DoormatSettings;
import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.arguments.IntegerArgumentType;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import com.mojang.brigadier.exceptions.SimpleCommandExceptionType;
import net.minecraft.block.BlockState;
import net.minecraft.command.argument.BlockPosArgumentType;
import net.minecraft.server.command.ServerCommandSource;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.text.Text;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.random.Random;

import static net.minecraft.server.command.CommandManager.argument;
import static net.minecraft.server.command.CommandManager.literal;

public class RandomTickCommand {

    public static void register(CommandDispatcher<ServerCommandSource> dispatcher) {
        dispatcher.register(literal("randomtick")
            .requires(player -> CommandHelper.canUseCommand(player, DoormatSettings.commandRandomTick))
            .then(argument("pos", BlockPosArgumentType.blockPos())
                .executes(context -> RandomTickCommand.execute(
                    context.getSource(),
                    BlockPosArgumentType.getLoadedBlockPos(context, "pos"),
                    1))
                .then(argument("count", IntegerArgumentType.integer(1, 4096))
                    .executes(context -> RandomTickCommand.execute(
                        context.getSource(),
                        BlockPosArgumentType.getLoadedBlockPos(context, "pos"),
                        IntegerArgumentType.getInteger(context, "count"))
                    )
                )
            )
        );
    }

    private static int execute(ServerCommandSource source, BlockPos pos, int count) throws CommandSyntaxException {
        ServerWorld world = source.getWorld();
        BlockState state = world.getBlockState(pos);
        Random random = world.getRandom();
        if (state.hasRandomTicks()) {
            for (int i = 0; i < count; i++)
                state.randomTick(world, pos, random);
            source.sendFeedback(() -> Text.translatable("carpet.command.randomTick.success", count, pos.getX(), pos.getY(), pos.getZ()), true);
            return 1;
        }
        else throw new SimpleCommandExceptionType(Text.translatable("carpet.command.randomTick.failed")).create();
    }

}
