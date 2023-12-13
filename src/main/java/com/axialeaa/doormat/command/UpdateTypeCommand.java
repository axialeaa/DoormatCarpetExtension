package com.axialeaa.doormat.command;

import carpet.utils.Messenger;
import com.axialeaa.doormat.DoormatSettings;
import com.axialeaa.doormat.helpers.CommandHelper;
import com.axialeaa.doormat.util.ConfigFile;
import com.axialeaa.doormat.util.UpdateTypeRules;
import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.arguments.StringArgumentType;
import net.minecraft.server.command.ServerCommandSource;

import static com.axialeaa.doormat.util.UpdateTypeRules.*;
import static net.minecraft.command.CommandSource.suggestMatching;
import static net.minecraft.server.command.CommandManager.argument;
import static net.minecraft.server.command.CommandManager.literal;

public class UpdateTypeCommand {

    public static final String ALIAS = "updatetype";

    public static void register(CommandDispatcher<ServerCommandSource> dispatcher) {
        dispatcher.register(literal(ALIAS)
            .requires(player -> carpet.utils.CommandHelper.canUseCommand(player, DoormatSettings.commandUpdateType))
            .then(argument("component", StringArgumentType.word())
                .suggests((ctx, builder) -> suggestMatching(UpdateTypeRules.getCommandSuggestions(), builder))
                .executes(ctx -> UpdateTypeCommand.get(
                    ctx.getSource(),
                    StringArgumentType.getString(ctx, "component"))
                )
                .then(argument("value", StringArgumentType.word())
                    .suggests((ctx, builder) -> suggestMatching(UpdateTypes.getCommandSuggestions(), builder))
                    .executes(ctx -> UpdateTypeCommand.set(
                        ctx.getSource(),
                        StringArgumentType.getString(ctx, "component"),
                        StringArgumentType.getString(ctx, "value"))
                    )
                )
            )
            .then(literal("reset")
                .executes(ctx -> UpdateTypeCommand.reset(
                    ctx.getSource())
                )
            )
        );
    }

    /**
     * Assigns a new enum value to the entered-in redstone component's update type hashmap, defined in {@link UpdateTypeRules}.
     */
    private static int set(ServerCommandSource source, String key, String input) {
        UpdateTypeRules component = ruleKeys.get(key);
        UpdateTypes enumInput = UpdateTypes.keys.get(input);
        // Commands can't take in enum entries, so we find the entry corresponding to the string using the update type's own hashmap.
        // There are a lot of hashmaps in this implementation. I apologise.

        if (CommandHelper.isExperimentalDatapackDisabled(source) && (component == CRAFTER || component == COPPER_BULB)) {
            Messenger.m(source, "r " + component.getPrettyName() + " is not enabled on this world!");
            return 0;
        }
        if (!DoormatSettings.redstoneOpensBarrels && component == BARREL) {
            Messenger.m(source, "r redstoneOpensBarrels is not enabled on this world!");
            return 0;
        }
        if (ruleValues.get(component) != enumInput) { // If the value assigned to the component via the values hashmap is not the same as the inputted argument...
            ruleValues.put(component, enumInput);
            Messenger.m(source, "w Set " + component.getPrettyName() + " update type to " + enumInput);
            ConfigFile.save(source.getServer());
            return 1;
        }
        else {
            Messenger.m(source, "r " + component.getPrettyName() + " update type is already set to " + enumInput);
            return 0;
        }
    }

    /**
     * Finds the value assigned to the entered-in redstone component's update type hashmap, defined in {@link UpdateTypeRules}.
     */
    private static int get(ServerCommandSource source, String key) {
        UpdateTypeRules component = ruleKeys.get(key);
        UpdateTypes value = ruleValues.get(component);

        if (CommandHelper.isExperimentalDatapackDisabled(source) && (component == CRAFTER || component == COPPER_BULB)) {
            Messenger.m(source, "r " + component.getPrettyName() + " is not enabled on this world!");
            return 0;
        }
        if (!DoormatSettings.redstoneOpensBarrels && component == BARREL) {
            Messenger.m(source, "r redstoneOpensBarrels is not enabled on this world!");
            return 0;
        }
        Messenger.m(source, "w " + component.getPrettyName() + " update type is set to " + value + (value == component.getDefaultValue() ? " (default value)" : " (modified value)"));
        return 1;
    }

    /**
     * Sets each redstone component's update type hashmap value to its default.
     */
    private static int reset(ServerCommandSource source) {
        boolean bl = false; // Define a new boolean
        for (UpdateTypeRules component : UpdateTypeRules.values()) // Iterate through a list of all enum entries...
            if (ruleValues.get(component) != component.getDefaultValue()) { // If the value has been modified...
                ruleValues.put(component, component.getDefaultValue()); // Set it to the default value
                bl = true; // Reassign the boolean to true, to indicate the hashmap has been changed
            }

        if (bl) { // If the hashmap had to change in order to restore default settings...
            Messenger.m(source, "w Restored vanilla update type settings");
            ConfigFile.save(source.getServer());
            return 1; // Success!
        }
        else {
            Messenger.m(source, "r Update type values haven't changed. Try tweaking some settings first!");
            return 0;
        }
    }

}
