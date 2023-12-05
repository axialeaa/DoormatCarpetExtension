package com.axialeaa.doormat.command;

import carpet.utils.CommandHelper;
import com.axialeaa.doormat.DoormatSettings;
import com.axialeaa.doormat.util.QuasiConnectivityRules;
import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.arguments.BoolArgumentType;
import com.mojang.brigadier.arguments.StringArgumentType;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import com.mojang.brigadier.exceptions.SimpleCommandExceptionType;
import net.minecraft.command.CommandSource;
import net.minecraft.server.command.ServerCommandSource;
import net.minecraft.text.Text;

import static com.axialeaa.doormat.util.QuasiConnectivityRules.ruleKeys;
import static com.axialeaa.doormat.util.QuasiConnectivityRules.ruleValues;
import static net.minecraft.server.command.CommandManager.argument;
import static net.minecraft.server.command.CommandManager.literal;

public class QuasiConnectivityCommand {

    public static void register(CommandDispatcher<ServerCommandSource> dispatcher) {
        dispatcher.register(literal("quasiconnectivity")
            .requires(player -> CommandHelper.canUseCommand(player, DoormatSettings.commandQC))
            .then(argument("component", StringArgumentType.word())
                .suggests((ctx, builder) -> CommandSource.suggestMatching(QuasiConnectivityRules.getCommandSuggestions(), builder))
                .executes(QuasiConnectivityCommand::get)
                .then(argument("value", BoolArgumentType.bool())
                    .executes(QuasiConnectivityCommand::set)))
            .then(literal("reset")
                .executes(QuasiConnectivityCommand::reset))
        );
    }

    /**
     * Assigns a new boolean value to the entered-in redstone component's quasi-connectivity hashmap, defined in {@link QuasiConnectivityRules}.
     * @throws CommandSyntaxException when the specified value matches the one present in the hashmap.
     */
    private static int set(CommandContext<ServerCommandSource> ctx) throws CommandSyntaxException {
        ServerCommandSource source = ctx.getSource();
        QuasiConnectivityRules component = ruleKeys.get((StringArgumentType.getString(ctx, "component")));
        // Get the enum entry assigned to the specified selection via the hashmap.
        boolean input = BoolArgumentType.getBool(ctx, "value");

        if (ruleValues.get(component) != input) { // If the value assigned to the component via the values hashmap is not the same as the inputted argument...
            ruleValues.put(component, input);
            source.sendFeedback(() -> Text.translatable("carpet.command.quasiConnectivity.set.success", component.getFormattedName(), input), true);
            return 1;
        }
        else throw new SimpleCommandExceptionType(Text.translatable("carpet.command.quasiConnectivity.set.failed", component.getFormattedName(), input)).create();
    }

    /**
     * Finds the value assigned to the entered-in redstone component's quasi-connectivity hashmap, defined in {@link QuasiConnectivityRules}.
     */
    private static int get(CommandContext<ServerCommandSource> ctx) {
        ServerCommandSource source = ctx.getSource();
        QuasiConnectivityRules component = ruleKeys.get((StringArgumentType.getString(ctx, "component")));
        boolean value = ruleValues.get(component);

        source.sendFeedback(() -> Text.translatable((value == component.getDefaultValue() ? "carpet.command.quasiConnectivity.get.unchanged" : "carpet.command.quasiConnectivity.get.changed"), component.getFormattedName(), value), true);
        return 1;
    }

    /**
     * Sets each redstone component's quasi-connectivity hashmap value to its default.
     * @throws CommandSyntaxException when all values are unchanged from their defaults.
     */
    private static int reset(CommandContext<ServerCommandSource> ctx) throws CommandSyntaxException {
        ServerCommandSource source = ctx.getSource();
        boolean bl = false; // Define a new boolean
        for (QuasiConnectivityRules component : QuasiConnectivityRules.values()) // Iterate through a list of all enum entries...
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
