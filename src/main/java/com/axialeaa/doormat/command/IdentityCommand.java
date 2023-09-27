package com.axialeaa.doormat.command;

import carpet.utils.CommandHelper;
import carpet.utils.Messenger;
import com.axialeaa.doormat.DoormatSettings;
import com.mojang.brigadier.CommandDispatcher;
import net.minecraft.server.command.ServerCommandSource;

import static net.minecraft.server.command.CommandManager.literal;

public class IdentityCommand {

    public static void register(CommandDispatcher<ServerCommandSource> dispatcher) {
        dispatcher.register(literal("identity")
                .requires(player -> CommandHelper.canUseCommand(player, DoormatSettings.commandIdentity))
                .then(literal("pronouns")
                        .executes(context -> { Messenger.m(context.getSource(), "pronouns"); return 1; })

                )
        );
    }

}