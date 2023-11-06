package com.axialeaa.doormat.command;

import carpet.utils.CommandHelper;
import com.axialeaa.doormat.DoormatSettings;
import com.mojang.brigadier.arguments.IntegerArgumentType;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import com.mojang.brigadier.exceptions.SimpleCommandExceptionType;
import com.mojang.brigadier.tree.LiteralCommandNode;
import net.fabricmc.fabric.api.command.v2.CommandRegistrationCallback;
import net.minecraft.block.BlockState;
import net.minecraft.command.argument.BlockPosArgumentType;
import net.minecraft.server.command.CommandManager;
import net.minecraft.server.command.ServerCommandSource;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.text.Text;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.random.Random;

/**
 * This class contains the code for all registered commands in Doormat. This syntax was provided in <a href="https://gist.github.com/falkreon/f58bb91e45ba558bc7fd827e81c6cb45">falkreon's gist</a> on the topic of Brigadier.
 */
public class DoormatCommands {

    public static void registerCommands() {
        // RANDOMTICK
        CommandRegistrationCallback.EVENT.register((dispatcher, registryAccess, environment) -> {
            LiteralCommandNode<ServerCommandSource> randomTickNode = CommandManager
                .literal("randomtick")
                .requires(player -> CommandHelper.canUseCommand(player, DoormatSettings.commandRandomTick))
                .build();
            var pos = CommandManager
                .argument("pos", BlockPosArgumentType.blockPos())
                .executes(context -> RandomTickCommand.execute(context.getSource(), BlockPosArgumentType.getLoadedBlockPos(context, "pos"), 1))
                .build();
            var count = CommandManager
                .argument("count", IntegerArgumentType.integer(1, 32768))
                .executes(context -> RandomTickCommand.execute(context.getSource(), BlockPosArgumentType.getLoadedBlockPos(context, "pos"), IntegerArgumentType.getInteger(context, "count")))
                .build();

            dispatcher.getRoot().addChild(randomTickNode);
            randomTickNode.addChild(pos);
            pos.addChild(count);
        });
    }

    public static class RandomTickCommand {
        public static int execute(ServerCommandSource source, BlockPos pos, int count) throws CommandSyntaxException {
            ServerWorld world = source.getWorld(); // get the world
            BlockState state = world.getBlockState(pos); // get the block state at the entered position
            Random random = world.getRandom();
            // if the block can be randomTicked...
            if (state.hasRandomTicks()) {
                for (int i = 0; i < count; i++)
                    state.randomTick(world, pos, random); // send x number of randomTicks to this position where x is the entered count value
                source.sendFeedback(() -> Text.translatable("carpet.command.randomTick.success", count, pos.getX(), pos.getY(), pos.getZ()), true);
                return 1; // output this success message, substituting each %s for these args, and return 1 to tell the game this was successful
            } else throw new SimpleCommandExceptionType(Text.translatable("carpet.command.randomTick.failed")).create();
            // if all else fails, throw an exception in the form of this error message
        }
    }

}