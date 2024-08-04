package com.axialeaa.doormat.command;

import carpet.utils.CommandHelper;
import carpet.utils.Messenger;
import com.axialeaa.doormat.DoormatServer;
import com.axialeaa.doormat.tinker_kit.ConfigFile;
import com.axialeaa.doormat.tinker_kit.TinkerKit;
import com.mojang.brigadier.Command;
import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.arguments.ArgumentType;
import com.mojang.brigadier.arguments.IntegerArgumentType;
import com.mojang.brigadier.context.CommandContext;
import net.minecraft.block.Block;
import net.minecraft.command.CommandRegistryAccess;
import net.minecraft.command.argument.RegistryEntryReferenceArgumentType;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.server.command.ServerCommandSource;
import org.apache.commons.lang3.StringUtils;

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.Objects;
import java.util.stream.Stream;

import static net.minecraft.command.CommandSource.suggestMatching;
import static net.minecraft.server.command.CommandManager.argument;
import static net.minecraft.server.command.CommandManager.literal;

/**
 * Defines an instance for a new Tinker Kit command, simplifying the registration process of a new one.<br>
 * <code>/alias list</code> - {@link AbstractTinkerKitCommand#list(ServerCommandSource)}.<br>
 * <code>/alias get &lt;component&gt;</code> - {@link AbstractTinkerKitCommand#get(ServerCommandSource, Block)}.<br>
 * <code>/alias set &lt;value&gt; all</code> - {@link AbstractTinkerKitCommand#setAll(ServerCommandSource, Object)}.<br>
 * <code>/alias set &lt;value&gt; &lt;component&gt;</code> - {@link AbstractTinkerKitCommand#set(ServerCommandSource, Block, Object)}.<br>
 * <code>/alias reset all</code> - {@link AbstractTinkerKitCommand#resetAll(ServerCommandSource)}.<br>
 * <code>/alias reset &lt;component&gt;</code> - {@link AbstractTinkerKitCommand#reset(ServerCommandSource, Block)}.<br>
 * <code>/alias file load</code> - {@link AbstractTinkerKitCommand#load(ServerCommandSource)}.<br>
 * <code>/alias file update</code> - {@link AbstractTinkerKitCommand#update(ServerCommandSource)}.
 * @param <T> The object type used as an input for the command.
 */
public abstract class AbstractTinkerKitCommand<T> {

    private Class<T> genericClass;

    /**
     * <a href="https://web.archive.org/web/20180124022935/http://blog.xebia.com/acessing-generic-types-at-runtime-in-java/">Accessing generic types at runtime in Java</a>
     */
    @SuppressWarnings("unchecked")
    AbstractTinkerKitCommand() {
        Type superClass = this.getClass().getGenericSuperclass();
        Type type = ((ParameterizedType) superClass).getActualTypeArguments()[0];

        try {
            this.genericClass = (Class<T>) Class.forName(type.getTypeName());
        }
        catch (ClassNotFoundException ignored) {}
    }

    /**
     * @return The {@link TinkerKit.Type} associated with this command. Controls the command aliases, chat outputs and maps, among other things.
     */
    public abstract TinkerKit.Type getType();

    /**
     * @return The argument type for this command's input value. Should be defined as a new instance of an argument type class, for example {@link IntegerArgumentType#integer()}.
     */
    public abstract ArgumentType<T> getArgumentType();

    /**
     * @return A stream of objects defining the autocompletion suggestions for the command. Converted to strings upon registration.
     */
    public abstract Stream<T> getSuggestions();

    public Object getInputValue(CommandContext<ServerCommandSource> ctx, String name, T value) {
        return value;
    }

    public void register(CommandDispatcher<ServerCommandSource> dispatcher, CommandRegistryAccess registryAccess) {
        dispatcher.register(literal(getType().name)
            .requires(source -> CommandHelper.canUseCommand(source, getType().commandRule))
            .then(literal("list").executes(ctx -> list(ctx.getSource())))
            .then(literal("get")
                .then(argument("block", RegistryEntryReferenceArgumentType.registryEntry(registryAccess, RegistryKeys.BLOCK))
                    .suggests((ctx, builder) -> suggestMatching(getType().getBlockKeys(), builder))
                    .executes(ctx -> get(ctx.getSource(),
                        RegistryEntryReferenceArgumentType.getRegistryEntry(ctx, "block", RegistryKeys.BLOCK).value()
                    ))
                )
            )
            .then(literal("set")
                .then(argument("value", getArgumentType())
                    .suggests((ctx, builder) -> suggestMatching(getSuggestions().map(Objects::toString), builder))
                    .then(literal("all")
                        .executes(ctx -> setAll(ctx.getSource(),
                            getInputValue(ctx, "value", ctx.getArgument("value", genericClass))
                        ))
                    )
                    .then(argument("block", RegistryEntryReferenceArgumentType.registryEntry(registryAccess, RegistryKeys.BLOCK))
                        .suggests((ctx, builder) -> suggestMatching(getType().getBlockKeys(), builder))
                        .executes(ctx -> set(ctx.getSource(),
                            RegistryEntryReferenceArgumentType.getRegistryEntry(ctx, "block", RegistryKeys.BLOCK).value(),
                            getInputValue(ctx, "value", ctx.getArgument("value", genericClass))
                        ))
                    )
                )
            )
            .then(literal("reset")
                .then(literal("all").executes(ctx -> resetAll(ctx.getSource())))
                .then(argument("block", RegistryEntryReferenceArgumentType.registryEntry(registryAccess, RegistryKeys.BLOCK))
                    .suggests((ctx, builder) -> suggestMatching(getType().getBlockKeys(), builder))
                    .executes(ctx -> reset(ctx.getSource(),
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
     * Assigns the inputted value to the entered-in redstone component.
     */
    private int set(ServerCommandSource source, Block block, Object input) {
        if (!getType().canModify(block)) {
            Messenger.m(source, String.format("r \"%s\" is not a valid component!", TinkerKit.getTranslatedName(block)));
            return 0;
        }

        if (input == null) {
            Messenger.m(source, String.format("r Invalid %s value!", getType().name));
            return 0;
        }

        if (getType().getModifiedValue(block).equals(input)) {
            Messenger.m(source, String.format("r %s %s value is already set to %s!", TinkerKit.getTranslatedName(block), getType().name, input));
            return 0;
        }
        else {
            getType().set(block, input);
            Messenger.m(source, String.format("w Set %s %s value to %s", TinkerKit.getTranslatedName(block), getType().name, input));

            return updateFile(source) ? Command.SINGLE_SUCCESS : 0;
        }
    }

    /**
     * Assigns the inputted value to every redstone component in this map.
     */
    private int setAll(ServerCommandSource source, Object input) {
        boolean wasModified = false;

        for (Block block : getType().getBlocks().toList()) {
            if (!getType().getModifiedValue(block).equals(input)) {
                getType().set(block, input);
                wasModified = true;
            }
        }

        if (wasModified) {
            Messenger.m(source, String.format("w Set all %s values to %s", getType().name, input));
            return updateFile(source) ? Command.SINGLE_SUCCESS : 0;
        }
        else {
            Messenger.m(source, String.format("r All %s values match %s. Try tweaking some settings first!", getType().name, input));
            return 0;
        }
    }

    /**
     * Finds and prints the value assigned to the inputted redstone component for this map to chat.
     */
    private int get(ServerCommandSource source, Block block) {
        if (!getType().canModify(block)) {
            Messenger.m(source, String.format("r %s is not a valid component!", TinkerKit.getTranslatedName(block)));
            return 0;
        }

        Object value = getType().getModifiedValue(block);
        Messenger.m(source, String.format("w %s %s value is set to %s (%s value)", TinkerKit.getTranslatedName(block), getType().name, value, getType().isDefaultValue(block) ? "default" : "modified"));

        return Command.SINGLE_SUCCESS;
    }

    /**
     * Finds and prints all values for this map to a list in chat.
     */
    private int list(ServerCommandSource source) {
        Messenger.m(source, "");

        if (getType().isMapModified()) {
            String cmd = String.format("?/%s reset all", getType().name);
            Messenger.m(source, String.format("bui %s values:", StringUtils.capitalize(getType().name)), cmd, "^g Restore default values?");
        }
        else Messenger.m(source, String.format("bu %s values:", StringUtils.capitalize(getType().name)));

        for (Block block : getType().getBlocks().toList()) {
            Object value = getType().getModifiedValue(block);
            String s = String.format("%s: %s", TinkerKit.getTranslatedName(block), value);

            if (getType().isDefaultValue(block))
                Messenger.m(source, String.format("g - %s (default value)", s));
            else {
                String cmd = String.format("?/%s reset %s", getType().name, TinkerKit.getKey(block));
                Messenger.m(source, "w - ", String.format("wi %s (modified value)", s), cmd, "^g Restore default value?");
            }
        }

        return Command.SINGLE_SUCCESS;
    }

    /**
     * Sets the inputted redstone component's value to the default for this map.
     */
    private int reset(ServerCommandSource source, Block block) {
        if (!getType().canModify(block)) {
            Messenger.m(source, String.format("r %s is not a valid component!", TinkerKit.getTranslatedName(block)));
            return 0;
        }

        if (getType().isDefaultValue(block)) {
            Messenger.m(source, String.format("r %s %s value is already set to default!", TinkerKit.getTranslatedName(block), getType().name));
            return 0;
        }
        else {
            getType().reset(block);
            Messenger.m(source, String.format("w Restored default %s %s value", TinkerKit.getTranslatedName(block), getType().name));

            return updateFile(source) ? Command.SINGLE_SUCCESS : 0;
        }
    }

    /**
     * Sets all values to their defaults for this map.
     */
    private int resetAll(ServerCommandSource source) {
        boolean wasModified = false;

        for (Block block : getType().getBlocks().toList()) {
            if (!getType().isDefaultValue(block)) {
                getType().reset(block);
                wasModified = true;
            }
        }

        if (wasModified) {
            Messenger.m(source, String.format("w Restored default %s values", getType().name));
            return updateFile(source) ? Command.SINGLE_SUCCESS : 0;
        }
        else {
            Messenger.m(source, String.format("r %s values are already set to default. Try tweaking some settings first!", StringUtils.capitalize(getType().name)));
            return 0;
        }
    }

    private int load(ServerCommandSource source) {
        if (ConfigFile.loadFromFile(source.getServer())) {
            Messenger.m(source, "w Successfully loaded values from config file");
            return Command.SINGLE_SUCCESS;
        }

        Messenger.m(source, "r Failed to load values from config file. Check the log output for more info!");
        return 0;
    }

    private int update(ServerCommandSource source) {
        if (!updateFile(source))
            return 0;

        Messenger.m(source, "w Successfully updated config file");
        return Command.SINGLE_SUCCESS;
    }

    private boolean updateFile(ServerCommandSource source) {
        if (ConfigFile.updateFile(source.getServer()))
            return true;

        Messenger.m(source, "r Failed to update config file. Check the log output for more info!");
        return false;
    }

}