package com.axialeaa.doormat.command;

import carpet.utils.CommandHelper;
import carpet.utils.Messenger;
import com.axialeaa.doormat.DoormatSettings;
import com.axialeaa.doormat.util.ConfigFile;
import com.axialeaa.doormat.util.QuasiConnectivityRules;
import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.arguments.BoolArgumentType;
import com.mojang.brigadier.arguments.StringArgumentType;
import net.minecraft.server.command.ServerCommandSource;

import static com.axialeaa.doormat.util.QuasiConnectivityRules.*;
import static net.minecraft.command.CommandSource.suggestMatching;
import static net.minecraft.server.command.CommandManager.argument;
import static net.minecraft.server.command.CommandManager.literal;

public class QuasiConnectivityCommand {

    public static final String ALIAS = "quasiconnectivity";

    public static void register(CommandDispatcher<ServerCommandSource> dispatcher) {
        dispatcher.register(literal(ALIAS)
            .requires(player -> CommandHelper.canUseCommand(player, DoormatSettings.commandQC))
            .then(argument("component", StringArgumentType.word())
                .suggests((ctx, builder) -> suggestMatching(QuasiConnectivityRules.getCommandSuggestions(), builder))
                .executes(ctx -> QuasiConnectivityCommand.get(
                    ctx.getSource(),
                    StringArgumentType.getString(ctx, "component"))
                )
                .then(argument("value", BoolArgumentType.bool())
                    .executes(ctx -> QuasiConnectivityCommand.set(
                        ctx.getSource(),
                        StringArgumentType.getString(ctx, "component"),
                        BoolArgumentType.getBool(ctx, "value"))
                    )
                )
            )
            .then(literal("reset")
                .executes(ctx -> QuasiConnectivityCommand.reset(
                    ctx.getSource())
                )
            )
        );
    }

    /**
     * Assigns a new boolean value to the entered-in redstone component's quasi-connectivity hashmap, defined in {@link QuasiConnectivityRules}.
     */
    private static int set(ServerCommandSource source, String key, boolean input) {
        QuasiConnectivityRules component = ruleKeys.get(key);
        // Get the enum entry assigned to the specified selection via the hashmap.

        if (ruleValues.get(component) != input) { // If the value assigned to the component via the values hashmap is not the same as the inputted argument...
            ruleValues.put(component, input);
            Messenger.m(source, "w Set " + component.getPrettyName() + " quasi-connectivity to " + input);
            ConfigFile.save(source.getServer());
            return 1;
        }
        else {
            Messenger.m(source, "r " + component.getPrettyName() + " quasi-connectivity is already set to " + input);
            return 0;
        }
    }

    /**
     * Finds the value assigned to the entered-in redstone component's quasi-connectivity hashmap, defined in {@link QuasiConnectivityRules}.
     */
    private static int get(ServerCommandSource source, String key) {
        QuasiConnectivityRules component = ruleKeys.get(key);
        boolean value = ruleValues.get(component);

        Messenger.m(source, "w " + component.getPrettyName() + " quasi-connectivity is set to " + value + (value == component.getDefaultValue() ? " (default value)" : " (modified value)"));
        return 1;
    }

    /**
     * Sets each redstone component's quasi-connectivity hashmap value to its default.
     */
    private static int reset(ServerCommandSource source) {
        boolean bl = false; // Define a new boolean
        StringBuilder sb = new StringBuilder();
        for (QuasiConnectivityRules component : QuasiConnectivityRules.values()) // Iterate through a list of all enum entries...
            if (ruleValues.get(component) != component.getDefaultValue()) { // If the value has been modified...
                ruleValues.put(component, component.getDefaultValue()); // Set it to the default value
                if (bl)
                    sb.append(", ");
                sb.append(component.getPrettyName());
                bl = true; // Reassign the boolean to true, to indicate the hashmap has been changed
            }

        if (bl) { // If the hashmap had to change in order to restore default settings...
            Messenger.m(source, "w Restored vanilla quasi-connectivity value(s), modifying " + sb);
            ConfigFile.save(source.getServer());
            return 1; // Success!
        }
        else {
            Messenger.m(source, "r Quasi-connectivity values match vanilla. Try tweaking some settings first!");
            return 0;
        }
    }

}
