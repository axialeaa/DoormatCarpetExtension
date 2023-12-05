package com.axialeaa.doormat.command;

import carpet.utils.CommandHelper;
import com.axialeaa.doormat.DoormatSettings;
import com.axialeaa.doormat.util.UpdateTypeRules;
import com.axialeaa.doormat.util.UpdateTypeRules.UpdateTypes;
import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.arguments.StringArgumentType;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import com.mojang.brigadier.exceptions.SimpleCommandExceptionType;
import net.minecraft.server.command.ServerCommandSource;
import net.minecraft.text.Text;

import static com.axialeaa.doormat.util.UpdateTypeRules.ruleKeys;
import static com.axialeaa.doormat.util.UpdateTypeRules.ruleValues;
import static net.minecraft.command.CommandSource.suggestMatching;
import static net.minecraft.server.command.CommandManager.argument;
import static net.minecraft.server.command.CommandManager.literal;

public class UpdateTypeCommand {

    public static void register(CommandDispatcher<ServerCommandSource> dispatcher) {
        dispatcher.register(literal("updatetype")
            .requires(player -> CommandHelper.canUseCommand(player, DoormatSettings.commandUpdateType))
            .then(argument("component", StringArgumentType.word())
                .suggests((ctx, builder) -> suggestMatching(UpdateTypeRules.getCommandSuggestions(), builder))
                .executes(UpdateTypeCommand::get)
                .then(argument("value", StringArgumentType.word())
                    .suggests((ctx, builder) -> suggestMatching(UpdateTypes.getCommandSuggestions(), builder))
                    .executes(UpdateTypeCommand::set)))
            .then(literal("reset")
                .executes(UpdateTypeCommand::reset))
        );
    }

    /**
     * Assigns a new enum value to the entered-in redstone component's update type hashmap, defined in {@link UpdateTypeRules}.
     * @throws CommandSyntaxException when the specified value matches the one present in the hashmap.
     */
    private static int set(CommandContext<ServerCommandSource> ctx) throws CommandSyntaxException {
        ServerCommandSource source = ctx.getSource();
        UpdateTypeRules component = ruleKeys.get((StringArgumentType.getString(ctx, "component")));
        UpdateTypes input = UpdateTypes.keys.get(StringArgumentType.getString(ctx, "value"));
        // Commands can't take in enum entries, so we find the entry corresponding to the string using the update type's own hashmap.
        // There are a lot of hashmaps in this implementation. I apologise.

        if (ruleValues.get(component) != input) { // If the value assigned to the component via the values hashmap is not the same as the inputted argument...
            ruleValues.put(component, input);
            source.sendFeedback(() -> Text.translatable("carpet.command.updateType.set.success", component.getFormattedName(), input), true);
            return 1;
        }
        else throw new SimpleCommandExceptionType(Text.translatable("carpet.command.updateType.set.failed", component.getFormattedName(), input)).create();
    }

    /**
     * Finds the value assigned to the entered-in redstone component's update type hashmap, defined in {@link UpdateTypeRules}.
     */
    private static int get(CommandContext<ServerCommandSource> ctx) {
        ServerCommandSource source = ctx.getSource();
        UpdateTypeRules component = ruleKeys.get((StringArgumentType.getString(ctx, "component")));
        UpdateTypes value = ruleValues.get(component);

        source.sendFeedback(() -> Text.translatable((value == component.getDefaultValue() ? "carpet.command.updateType.get.unchanged" : "carpet.command.updateType.get.changed"), component.getFormattedName(), value), true);
        return 1;
    }

    /**
     * Sets each redstone component's update type hashmap value to its default.
     * @throws CommandSyntaxException when all values are unchanged from their defaults.
     */
    private static int reset(CommandContext<ServerCommandSource> ctx) throws CommandSyntaxException {
        ServerCommandSource source = ctx.getSource();
        boolean bl = false; // Define a new boolean
        for (UpdateTypeRules component : UpdateTypeRules.values()) // Iterate through a list of all enum entries...
            if (ruleValues.get(component) != component.getDefaultValue()) { // If the value has been modified...
                ruleValues.put(component, component.getDefaultValue()); // Set it to the default value
                bl = true; // Reassign the boolean to true, to indicate the hashmap has been changed
            }

        if (bl) { // If the hashmap had to change in order to restore default settings...
            source.sendFeedback(() -> Text.translatable("carpet.command.quasiConnectivity.reset.success"), true);
            return 1; // Success!
        }
        else throw new SimpleCommandExceptionType(Text.translatable("carpet.command.quasiConnectivity.reset.failed")).create();
    }

}
