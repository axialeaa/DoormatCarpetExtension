package com.axialeaa.doormat.command;

/*
import carpet.utils.CommandHelper;
import com.axialeaa.doormat.DoormatSettings;
import com.mojang.brigadier.arguments.StringArgumentType;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.tree.ArgumentCommandNode;
import com.mojang.brigadier.tree.LiteralCommandNode;
import net.fabricmc.fabric.api.command.v2.CommandRegistrationCallback;
import net.minecraft.command.CommandSource;
import net.minecraft.command.argument.EntityArgumentType;
import net.minecraft.server.command.CommandManager;
import net.minecraft.server.command.ServerCommandSource;
*/

public class DoormatCommands {

    /*
    public static void register() {
        CommandRegistrationCallback.EVENT.register((dispatcher, registryAccess, environment) -> {

            LiteralCommandNode<ServerCommandSource> pronounNode = CommandManager.literal("pronoun").requires(player -> CommandHelper.canUseCommand(player, DoormatSettings.commandPronoun)).build();
                LiteralCommandNode<ServerCommandSource> setNode = CommandManager.literal("set").executes(PronounCommand::set).build();
                    ArgumentCommandNode<ServerCommandSource, String> subjectiveNode = CommandManager.argument("subjective", StringArgumentType.word()).suggests((context, builder) -> CommandSource.suggestMatching(new String[] { "any","he", "it", "they", "she" }, builder)).executes(PronounCommand::subjective).build();
                    ArgumentCommandNode<ServerCommandSource, String> objectiveNode = CommandManager.argument("objective", StringArgumentType.word()).suggests((context, builder) -> CommandSource.suggestMatching(new String[] { "any", "her", "him", "it", "them" }, builder)).executes(PronounCommand::objective).build();
                    ArgumentCommandNode<ServerCommandSource, String> possessiveNode = CommandManager.argument("possessive", StringArgumentType.word()).suggests((context, builder) -> CommandSource.suggestMatching(new String[] { "any", "hers", "his", "its", "theirs" }, builder)).executes(PronounCommand::possessive).build();
                    ArgumentCommandNode<ServerCommandSource, String> reflexiveNode = CommandManager.argument("reflexive", StringArgumentType.word()).suggests((context, builder) -> CommandSource.suggestMatching(new String[] { "any", "herself", "himself", "itself", "themselves" }, builder)).executes(PronounCommand::reflexive).build();
                LiteralCommandNode<ServerCommandSource> unsetNode = CommandManager.literal("unset").executes(PronounCommand::unset).build();
                var playerArg = CommandManager.argument("player", EntityArgumentType.player()).executes(PronounCommand::player).build();
                    LiteralCommandNode<ServerCommandSource> permissionUnsetNode = CommandManager.literal("unset").requires(player -> player.hasPermissionLevel(4)).executes(PronounCommand::unset).build();

            //usage: /pronoun [set|unset|player]
            dispatcher.getRoot().addChild(pronounNode);
                pronounNode.addChild(setNode);
                    setNode.addChild(subjectiveNode);
                    subjectiveNode.addChild(objectiveNode);
                    objectiveNode.addChild(possessiveNode);
                    possessiveNode.addChild(reflexiveNode);
                pronounNode.addChild(unsetNode);
                pronounNode.addChild(playerArg);
                    playerArg.addChild(permissionUnsetNode);
        });
    }

    public static class PronounCommand {

        public static int set(CommandContext<ServerCommandSource> context) {
            return 1;
        }
            public static int subjective(CommandContext<ServerCommandSource> context) {
                return 1;
            }
            public static int objective(CommandContext<ServerCommandSource> context) {
                return 1;
            }
            public static int possessive(CommandContext<ServerCommandSource> context) {
                return 1;
            }
            public static int reflexive(CommandContext<ServerCommandSource> context) {
                return 1;
            }

        public static int unset(CommandContext<ServerCommandSource> context) {
            return 1;
        }
        public static int player(CommandContext<ServerCommandSource> context) {
            return 1;
        }

    }*/

}