package com.axialeaa.doormat.command;

import carpet.utils.CommandHelper;
import carpet.utils.Messenger;
import com.axialeaa.doormat.DoormatSettings;
import com.axialeaa.doormat.util.ConfigFile;
import com.axialeaa.doormat.util.RedstoneRule;
import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.arguments.StringArgumentType;
import net.minecraft.server.command.ServerCommandSource;

import static com.axialeaa.doormat.util.RedstoneRule.*;
import static net.minecraft.command.CommandSource.suggestMatching;
import static net.minecraft.server.command.CommandManager.argument;
import static net.minecraft.server.command.CommandManager.literal;

public class UpdateTypeCommand {

    public static final String ALIAS = "updatetype";

    public static void register(CommandDispatcher<ServerCommandSource> dispatcher) {
        dispatcher.register(literal(ALIAS)
            .requires(player -> CommandHelper.canUseCommand(player, DoormatSettings.commandUpdateType))
            .then(argument("component", StringArgumentType.word())
                .suggests((ctx, builder) -> suggestMatching(RedstoneRule.getUpdateTypeCommandSuggestions(), builder))
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
     * Assigns a new enum value to the entered-in redstone component's update type hashmap, defined in {@link RedstoneRule}.
     */
    private static int set(ServerCommandSource source, String key, String input) {
        if (!updateTypeKeys.containsKey(key)) {
            Messenger.m(source, "r \"" + key + "\" is not a valid component!");
            return 0;
        }

        if (!UpdateTypes.keys.containsKey(input)) {
            Messenger.m(source, "r \"" + input + "\" is not a valid update type!");
            return 0;
        }

        RedstoneRule component = updateTypeKeys.get(key);
        UpdateTypes enumInput = UpdateTypes.keys.get(input);
        // Commands can't take in enum constants, so we find the constant corresponding to the string using the update type's own hashmap.
        // There are a lot of hashmaps in this implementation. I apologise.

        if (updateTypeValues.get(component) != enumInput) { // If the value assigned to the component via the values hashmap is not the same as the inputted argument...
            updateTypeValues.put(component, enumInput);
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
     * Finds the value assigned to the entered-in redstone component's update type hashmap, defined in {@link RedstoneRule}.
     */
    private static int get(ServerCommandSource source, String key) {
        if (!updateTypeKeys.containsKey(key)) {
            Messenger.m(source, "r \"" + key + "\" is not a valid component!");
            return 0;
        }

        RedstoneRule component = updateTypeKeys.get(key);
        UpdateTypes value = updateTypeValues.get(component);

        Messenger.m(source, "w " + component.getPrettyName() + " update type is set to " + value + (value == component.getDefaultUpdateTypeValue() ? " (default value)" : " (modified value)"));
        return 1;
    }

    /**
     * Sets each redstone component's update type hashmap value to its default.
     */
    private static int reset(ServerCommandSource source) {
        boolean bl = false; // Define a new boolean
        StringBuilder sb = new StringBuilder(); // Create a string builder which will be used for creating a list of modified components ("Bell, Crafter, Dispenser and Dropper, Trapdoor")
        for (RedstoneRule component : RedstoneRule.values()) // Iterate through a list of all enum constants...
            if (updateTypeValues.get(component) != component.getDefaultUpdateTypeValue()) { // If the value has been modified...
                updateTypeValues.put(component, component.getDefaultUpdateTypeValue()); // Set it to the default value
                if (bl)
                    sb.append(", ");
                sb.append(component.getPrettyName());
                bl = true; // Reassign the boolean to true, to indicate the hashmap has been changed
            }

        if (bl) { // If the hashmap had to change in order to restore default settings...
            Messenger.m(source, "w Restored vanilla update type value(s), modifying " + sb);
            ConfigFile.save(source.getServer());
            return 1; // Success!
        }
        else {
            Messenger.m(source, "r Update type values match vanilla. Try tweaking some settings first!");
            return 0;
        }
    }

}
