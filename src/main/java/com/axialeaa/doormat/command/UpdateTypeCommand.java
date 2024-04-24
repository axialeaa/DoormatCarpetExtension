package com.axialeaa.doormat.command;

import carpet.utils.CommandHelper;
import carpet.utils.Messenger;
import com.axialeaa.doormat.DoormatServer;
import com.axialeaa.doormat.DoormatSettings;
import com.axialeaa.doormat.tinker_kit.ConfigFile;
import com.axialeaa.doormat.tinker_kit.TinkerKit;
import com.axialeaa.doormat.util.UpdateType;
import com.mojang.brigadier.Command;
import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.arguments.StringArgumentType;
import net.minecraft.block.Block;
import net.minecraft.command.CommandRegistryAccess;
import net.minecraft.command.argument.RegistryEntryReferenceArgumentType;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.server.command.ServerCommandSource;
import org.apache.commons.lang3.StringUtils;

import java.util.Arrays;
import java.util.Locale;

import static com.axialeaa.doormat.tinker_kit.TinkerKit.*;
import static net.minecraft.command.CommandSource.suggestMatching;
import static net.minecraft.server.command.CommandManager.argument;
import static net.minecraft.server.command.CommandManager.literal;

/**
 * <code>/updatetype list</code> - Finds and displays all update type values.<br>
 * <code>/updatetype get &lt;component&gt;</code> - Finds and displays the <code>component</code>'s update type value.<br>
 * <code>/updatetype set &lt;value&gt; all</code> - Sets all update type values to <code>value</code>.<br>
 * <code>/updatetype set &lt;value&gt; &lt;component&gt;</code> - Sets the <code>component</code>'s update type value to <code>value</code>.<br>
 * <code>/updatetype reset all</code> - Resets all update type values to default.<br>
 * <code>/updatetype reset &lt;component&gt;</code> - Resets the <code>component</code>'s update type value to default.<br>
 * <code>/updatetype file load</code> - Loads all quasi-connectivity and update type values from the config file.
 * <code>/updatetype file update</code> - Updates the config file with any new quasi-connectivity or update type value modifications.
 */
public class UpdateTypeCommand {

    public static final String ALIAS = "updatetype";

    public static void register(CommandDispatcher<ServerCommandSource> dispatcher, CommandRegistryAccess registryAccess) {
        dispatcher.register(literal(ALIAS)
            .requires(player -> CommandHelper.canUseCommand(player, DoormatSettings.commandUpdateType))
            .then(literal("list")
                .executes(ctx -> list(ctx.getSource()))
            )
            .then(literal("get")
                .then(argument("block", RegistryEntryReferenceArgumentType.registryEntry(registryAccess, RegistryKeys.BLOCK))
                    .suggests((ctx, builder) -> suggestMatching(getModifiableBlockKeys(ModificationType.UPDATE_TYPE), builder))
                    .executes(ctx -> get(
                        ctx.getSource(),
                        RegistryEntryReferenceArgumentType.getRegistryEntry(ctx, "block", RegistryKeys.BLOCK).value()
                    ))
                )
            )
            .then(literal("set")
                .then(argument("value", StringArgumentType.string())
                    .suggests((ctx, builder) -> suggestMatching(Arrays.stream(UpdateType.values()).map(UpdateType::asString), builder))
                    .then(literal("all")
                        .executes(ctx -> setAll(
                            ctx.getSource(),
                            StringArgumentType.getString(ctx, "value")
                        ))
                    )
                    .then(argument("block", RegistryEntryReferenceArgumentType.registryEntry(registryAccess, RegistryKeys.BLOCK))
                        .suggests((ctx, builder) -> suggestMatching(getModifiableBlockKeys(ModificationType.UPDATE_TYPE), builder))
                        .executes(ctx -> set(
                            ctx.getSource(),
                            RegistryEntryReferenceArgumentType.getRegistryEntry(ctx, "block", RegistryKeys.BLOCK).value(),
                            StringArgumentType.getString(ctx, "value")
                        ))
                    )
                )
            )
            .then(literal("reset")
                .then(literal("all")
                    .executes(ctx -> resetAll(ctx.getSource()))
                )
                .then(argument("block", RegistryEntryReferenceArgumentType.registryEntry(registryAccess, RegistryKeys.BLOCK))
                    .suggests((ctx, builder) -> suggestMatching(getModifiableBlockKeys(ModificationType.UPDATE_TYPE), builder))
                    .executes(ctx -> reset(
                        ctx.getSource(),
                        RegistryEntryReferenceArgumentType.getRegistryEntry(ctx, "block", RegistryKeys.BLOCK).value()
                    ))
                )
            )
            .then(literal("file")
                .requires(source -> DoormatServer.IS_DEBUG)
                .then(literal("load").executes(ctx -> QuasiConnectivityCommand.load(ctx.getSource())))
                .then(literal("update").executes(ctx -> QuasiConnectivityCommand.update(ctx.getSource())))
            )
        );
    }

    /**
     * Assigns the inputted value to the entered-in redstone component via {@link TinkerKit#MODIFIED_UPDATE_TYPE_VALUES}.
     */
    private static int set(ServerCommandSource source, Block block, String input) {
        if (!isModifiable(block, ModificationType.UPDATE_TYPE)) {
            Messenger.m(source, "r " + getTranslatedName(block) + " is not a valid component!");
            return 0;
        }

        UpdateType updateType;

        try {
            updateType = UpdateType.valueOf(input.toUpperCase(Locale.ROOT));
        }
        catch (IllegalArgumentException e) {
            Messenger.m(source, "r " + StringUtils.capitalize(input) + " is not a valid update type!");
            return 0;
        }

        if (MODIFIED_UPDATE_TYPE_VALUES.get(block) == updateType) {
            Messenger.m(source, "r " + getTranslatedName(block) + " update type is already set to " + updateType);
            return 0;
        }
        else {
            MODIFIED_UPDATE_TYPE_VALUES.put(block, updateType);
            Messenger.m(source, "w Set " + getTranslatedName(block) + " update type to " + updateType);
            ConfigFile.updateFile(source.getServer());

            return Command.SINGLE_SUCCESS;
        }
    }

    /**
     * Assigns the inputted update type value to every redstone component in {@link TinkerKit}.
     */
    private static int setAll(ServerCommandSource source, String input) {
        UpdateType updateType;

        try {
            updateType = UpdateType.valueOf(input.toUpperCase(Locale.ROOT));
        }
        catch (IllegalArgumentException e) {
            Messenger.m(source, "r " + StringUtils.capitalize(input) + " is not a valid update type!");
            return 0;
        }

        boolean wasModified = false;

        for (Block block : getModifiableBlocks(ModificationType.UPDATE_TYPE).toList()) {
            if (MODIFIED_UPDATE_TYPE_VALUES.get(block) != updateType) {
                MODIFIED_UPDATE_TYPE_VALUES.put(block, updateType);
                wasModified = true;
            }
        }

        if (wasModified) {
            Messenger.m(source, "w Set all update type values to " + updateType);
            ConfigFile.updateFile(source.getServer());

            return Command.SINGLE_SUCCESS;
        }
        else {
            Messenger.m(source, "r All update type values match" + updateType + ". Try tweaking some settings first!");
            return 0;
        }
    }

    /**
     * Finds and prints the update type value assigned to the inputted redstone component via {@link TinkerKit#MODIFIED_UPDATE_TYPE_VALUES}.
     */
    private static int get(ServerCommandSource source, Block block) {
        if (!isModifiable(block, ModificationType.UPDATE_TYPE)) {
            Messenger.m(source, "r " + getTranslatedName(block) + " is not a valid component!");
            return 0;
        }

        UpdateType value = MODIFIED_UPDATE_TYPE_VALUES.get(block);
        Messenger.m(source, "w " + getTranslatedName(block) + " update type is set to " + value + (isDefaultValue(block, ModificationType.UPDATE_TYPE) ? " (default value)" : " (modified value)"));

        return Command.SINGLE_SUCCESS;
    }

    /**
     * Finds and prints all update type values to a list in chat.
     */
    private static int list(ServerCommandSource source) {
        Messenger.m(source, "");
        if (isMapModified(ModificationType.UPDATE_TYPE))
            Messenger.m(source, "bui Update type values:", "?/" + ALIAS + " reset all", "^g Restore default values?");
        else
            Messenger.m(source, "bu Update type values:");

        for (Block block : getModifiableBlocks(ModificationType.UPDATE_TYPE).toList()) {
            UpdateType value = MODIFIED_UPDATE_TYPE_VALUES.get(block);

            String string = getTranslatedName(block) + ": " + value;
            String command = "?/" + ALIAS + " reset " + getKey(block);

            if (isDefaultValue(block, ModificationType.UPDATE_TYPE))
                Messenger.m(source, "g - " + string + " (default value)");
            else
                Messenger.m(source, "w - ", "wi " + string + " (modified value)", command, "^g Restore default value?");
        }

        return Command.SINGLE_SUCCESS;
    }

    /**
     * Sets the inputted redstone component's update type value to default.
     */
    private static int reset(ServerCommandSource source, Block block) {
        if (!isModifiable(block, ModificationType.UPDATE_TYPE)) {
            Messenger.m(source, "r " + getTranslatedName(block) + " is not a valid component!");
            return 0;
        }

        if (isDefaultValue(block, ModificationType.UPDATE_TYPE)) {
            Messenger.m(source, "r " + getTranslatedName(block) + " update type value is already set to default!");
            return 0;
        }
        else {
            setDefaultValue(block, ModificationType.UPDATE_TYPE);
            Messenger.m(source, "w Restored default " + getTranslatedName(block) + " update type value");
            ConfigFile.updateFile(source.getServer());

            return Command.SINGLE_SUCCESS;
        }
    }

    /**
     * Sets all redstone components' update type values to default.
     */
    private static int resetAll(ServerCommandSource source) {
        boolean wasModified = false;

        for (Block block : getModifiableBlocks(ModificationType.UPDATE_TYPE).toList()) {
            if (!isDefaultValue(block, ModificationType.UPDATE_TYPE)) {
                setDefaultValue(block, ModificationType.UPDATE_TYPE);
                wasModified = true;
            }
        }

        if (wasModified) {
            Messenger.m(source, "w Restored default update type values");
            ConfigFile.updateFile(source.getServer());

            return Command.SINGLE_SUCCESS;
        }
        else {
            Messenger.m(source, "r Update type values are already set to default. Try tweaking some settings first!");
            return 0;
        }
    }

}
