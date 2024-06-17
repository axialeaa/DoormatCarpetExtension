package com.axialeaa.doormat.command;

import carpet.utils.CommandHelper;
import carpet.utils.Messenger;
import com.axialeaa.doormat.DoormatServer;
import com.axialeaa.doormat.DoormatSettings;
import com.axialeaa.doormat.tinker_kit.ConfigFile;
import com.axialeaa.doormat.tinker_kit.TinkerKit;
import com.mojang.brigadier.Command;
import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.arguments.IntegerArgumentType;
import net.minecraft.block.Block;
import net.minecraft.command.CommandRegistryAccess;
import net.minecraft.command.argument.RegistryEntryReferenceArgumentType;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.server.command.ServerCommandSource;

import static com.axialeaa.doormat.tinker_kit.TinkerKit.*;
import static net.minecraft.command.CommandSource.suggestMatching;
import static net.minecraft.server.command.CommandManager.argument;
import static net.minecraft.server.command.CommandManager.literal;

/**
 * <code>/quasiconnectivity list</code> - Finds and displays all quasi-connectivity values.<br>
 * <code>/quasiconnectivity get &lt;component&gt;</code> - Finds and displays the <code>component</code>'s quasi-connectivity value.<br>
 * <code>/quasiconnectivity set &lt;value&gt; all</code> - Sets all quasi-connectivity values to <code>value</code>.<br>
 * <code>/quasiconnectivity set &lt;value&gt; &lt;component&gt;</code> - Sets the <code>component</code>'s quasi-connectivity value to <code>value</code>.<br>
 * <code>/quasiconnectivity reset all</code> - Resets all quasi-connectivity values to default.<br>
 * <code>/quasiconnectivity reset &lt;component&gt;</code> - Resets the <code>component</code>'s quasi-connectivity value to default.<br>
 * <code>/quasiconnectivity file load</code> - Loads all quasi-connectivity and update type values from the config file.<br>
 * <code>/quasiconnectivity file update</code> - Updates the config file with any new quasi-connectivity or update type value modifications.
 */
public class QuasiConnectivityCommand {

    public static final String ALIAS = "quasiconnectivity";

    public static void register(CommandDispatcher<ServerCommandSource> dispatcher, CommandRegistryAccess registryAccess) {
        dispatcher.register(literal(ALIAS)
            .requires(player -> CommandHelper.canUseCommand(player, DoormatSettings.commandQC))
            .then(literal("list")
                .executes(ctx -> list(ctx.getSource()))
            )
            .then(literal("get")
                .then(argument("block", RegistryEntryReferenceArgumentType.registryEntry(registryAccess, RegistryKeys.BLOCK))
                    .suggests((ctx, builder) -> suggestMatching(Type.QC.getBlockKeys(), builder))
                    .executes(ctx -> get(
                        ctx.getSource(),
                        RegistryEntryReferenceArgumentType.getRegistryEntry(ctx, "block", RegistryKeys.BLOCK).value()
                    ))
                )
            )
            .then(literal("set")
                .then(argument("value", IntegerArgumentType.integer(0))
                    .suggests((ctx, builder) -> suggestMatching(new String[]{
                        String.valueOf(0),
                        String.valueOf(1)
                    }, builder))
                    .then(literal("all")
                        .executes(ctx -> setAll(
                            ctx.getSource(),
                            IntegerArgumentType.getInteger(ctx, "value")
                        ))
                    )
                    .then(argument("block", RegistryEntryReferenceArgumentType.registryEntry(registryAccess, RegistryKeys.BLOCK))
                        .suggests((ctx, builder) -> suggestMatching(Type.QC.getBlockKeys(), builder))
                        .executes(ctx -> set(
                            ctx.getSource(),
                            RegistryEntryReferenceArgumentType.getRegistryEntry(ctx, "block", RegistryKeys.BLOCK).value(),
                            IntegerArgumentType.getInteger(ctx, "value")
                        ))
                    )
                )
            )
            .then(literal("reset")
                .then(literal("all")
                    .executes(ctx -> resetAll(ctx.getSource()))
                )
                .then(argument("block", RegistryEntryReferenceArgumentType.registryEntry(registryAccess, RegistryKeys.BLOCK))
                    .suggests((ctx, builder) -> suggestMatching(Type.QC.getBlockKeys(), builder))
                    .executes(ctx -> reset(
                        ctx.getSource(),
                        RegistryEntryReferenceArgumentType.getRegistryEntry(ctx, "block", RegistryKeys.BLOCK).value()
                    ))
                )
            )
            .then(literal("file")
                .requires(source -> DoormatServer.IS_DEBUG)
                .then(literal("load").executes(ctx -> load(ctx.getSource())))
                .then(literal("update").executes(ctx -> update(ctx.getSource())))
            )
        );
    }

    /**
     * Assigns the inputted value to the entered-in redstone component via {@link TinkerKit#MODIFIED_QC_VALUES}.
     */
    private static int set(ServerCommandSource source, Block block, int input) {
        if (!Type.QC.canModify(block)) {
            Messenger.m(source, "r " + getTranslatedName(block) + " is not a valid component!");
            return 0;
        }

        if ((int) Type.QC.getModifiedValue(block) == input) {
            Messenger.m(source, "r " + getTranslatedName(block) + " quasi-connectivity range is already set to " + input);
            return 0;
        }
        else {
            Type.QC.set(block, input);

            Messenger.m(source, "w Set " + getTranslatedName(block) + " quasi-connectivity range to " + input);
            ConfigFile.updateFile(source.getServer());

            return Command.SINGLE_SUCCESS;
        }
    }

    /**
     * Assigns the inputted quasi-connectivity value to every redstone component in {@link TinkerKit}.
     */
    private static int setAll(ServerCommandSource source, int input) {
        boolean wasModified = false;

        for (Block block : Type.QC.getBlocks().toList()) {
            if ((int) Type.QC.getModifiedValue(block) != input) {
                Type.QC.set(block, input);
                wasModified = true;
            }
        }

        if (wasModified) {
            Messenger.m(source, "w Set all quasi-connectivity values to " + input);
            ConfigFile.updateFile(source.getServer());
            return Command.SINGLE_SUCCESS;
        }
        else {
            Messenger.m(source, "r All quasi-connectivity values match" + input + ". Try tweaking some settings first!");
            return 0;
        }
    }

    /**
     * Finds and prints the quasi-connectivity value assigned to the inputted redstone component via {@link TinkerKit#MODIFIED_QC_VALUES}.
     */
    private static int get(ServerCommandSource source, Block block) {
        if (!Type.QC.canModify(block)) {
            Messenger.m(source, "r " + getTranslatedName(block) + " is not a valid component!");
            return 0;
        }

        int value = (int) Type.QC.getModifiedValue(block);
        Messenger.m(source, "w " + getTranslatedName(block) + " quasi-connectivity range is set to " + value + (Type.QC.isDefaultValue(block) ? " (default value)" : " (modified value)"));

        return Command.SINGLE_SUCCESS;
    }

    /**
     * Finds and prints all quasi-connectivity values to a list in chat.
     */
    private static int list(ServerCommandSource source) {
        Messenger.m(source, "");
        if (Type.QC.isMapModified())
            Messenger.m(source, "bui Quasi-connectivity values:", "?/" + ALIAS + " reset all", "^g Restore default values?");
        else
            Messenger.m(source, "bu Quasi-connectivity values:");

        for (Block block : Type.QC.getBlocks().toList()) {
            String value = getTranslatedName(block) + ": " + Type.QC.getModifiedValue(block);
            String command = "?/" + ALIAS + " reset " + getKey(block);

            if (Type.QC.isDefaultValue(block))
                Messenger.m(source, "g - " + value + " (default value)");
            else
                Messenger.m(source, "w - ", "wi " + value + " (modified value)", command, "^g Restore default value?");
        }

        return Command.SINGLE_SUCCESS;
    }

    /**
     * Sets the inputted redstone component's quasi-connectivity value to default.
     */
    private static int reset(ServerCommandSource source, Block block) {
        if (!Type.QC.canModify(block)) {
            Messenger.m(source, "r " + getTranslatedName(block) + " is not a valid component!");
            return 0;
        }

        if (Type.QC.isDefaultValue(block)) {
            Messenger.m(source, "r " + getTranslatedName(block) + " quasi-connectivity value is already set to default!");
            return 0;
        }
        else {
            Type.QC.reset(block);
            Messenger.m(source, "w Restored default " + getTranslatedName(block) + " quasi-connectivity value");
            ConfigFile.updateFile(source.getServer());

            return Command.SINGLE_SUCCESS;
        }
    }

    /**
     * Sets all redstone components' quasi-connectivity values to default.
     */
    private static int resetAll(ServerCommandSource source) {
        boolean wasModified = false;

        for (Block block : Type.QC.getBlocks().toList()) {
            if (!Type.QC.isDefaultValue(block)) {
                Type.QC.reset(block);
                wasModified = true;
            }
        }

        if (wasModified) {
            Messenger.m(source, "w Restored default quasi-connectivity values");
            ConfigFile.updateFile(source.getServer());

            return Command.SINGLE_SUCCESS;
        }
        else {
            Messenger.m(source, "r Quasi-connectivity values are already set to default. Try tweaking some settings first!");
            return 0;
        }
    }

    static int load(ServerCommandSource source) {
        ConfigFile.loadFromFile(source.getServer());
        Messenger.m(source, "w Loaded values from config file!");

        return Command.SINGLE_SUCCESS;
    }

    static int update(ServerCommandSource source) {
        ConfigFile.updateFile(source.getServer());
        Messenger.m(source, "w Updated config file!");

        return Command.SINGLE_SUCCESS;
    }

}
