package com.axialeaa.doormat.command.tinker_kit;

import carpet.utils.CommandHelper;
import carpet.utils.Messenger;
import com.axialeaa.doormat.DoormatServer;
import com.axialeaa.doormat.tinker_kit.ConfigFile;
import com.axialeaa.doormat.tinker_kit.TinkerKit;
import com.mojang.brigadier.Command;
import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.arguments.ArgumentType;
import com.mojang.brigadier.arguments.IntegerArgumentType;
import net.minecraft.block.Block;
import net.minecraft.command.CommandRegistryAccess;
import net.minecraft.command.argument.RegistryEntryReferenceArgumentType;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.server.command.ServerCommandSource;
import org.apache.commons.lang3.StringUtils;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Stream;
import static net.minecraft.command.CommandSource.suggestMatching;
import static net.minecraft.server.command.CommandManager.argument;
import static net.minecraft.server.command.CommandManager.literal;

/**
 * Defines an instance for a new Tinker Kit command, simplifying the registration process of a new one.
 * @param <T> The object entries used as an input for the command.
 */
public abstract class AbstractTinkerKitCommand<T> {

    private final String ALIAS = getType().name;

    /**
     * @return The {@link TinkerKit.Type} associated with this command. Controls the command aliases, chat outputs and maps, among other things.
     */
    public abstract TinkerKit.Type getType();

    /**
     * @return The argument entries for this command's input value. Should be defined as a new instance of an argument entries class, for example {@link IntegerArgumentType#integer()}.
     */
    public abstract ArgumentType<T> getArgumentType();

    /**
     * @return The class associated with the entries parameter {@code T}.
     */
    public abstract Class<T> getObjectClass();

    /**
     * @return A stream of objects defining the autocompletion suggestions for the command.
     */
    public Stream<T> getSuggestions() {
        Map<Block, Object> defaults = this.getType().getDefaultMap();
        Stream<Object> values = defaults.values().stream();

        values = values.filter(Objects::nonNull);
        Stream<T> castedValues = values.map(object -> getObjectClass().cast(object));

        return castedValues.distinct();
    }

    /**
     * @param argument The command argument as specified by {@link AbstractTinkerKitCommand#getArgumentType()}.
     * @return An acceptable value for the associated Tinker Kit map, manipulating the command argument passed into {@code argument}.
     */
    public Object getInputValue(T argument) {
        return argument;
    }

    /**
     *<pre> .
     *└── /{@link AbstractTinkerKitCommand#ALIAS}
     *    ├──{@link AbstractTinkerKitCommand#list(ServerCommandSource) list}
     *    ├── get
     *    │   └──{@link AbstractTinkerKitCommand#get(ServerCommandSource, Block) &lt;block&gt;}
     *    ├── set
     *    │   └── &lt;value&gt;
     *    │       ├──{@link AbstractTinkerKitCommand#setAll(ServerCommandSource, Object) all}
     *    │       └──{@link AbstractTinkerKitCommand#set(ServerCommandSource, Block, Object) &lt;block&gt;}
     *    ├── reset
     *    │   ├──{@link AbstractTinkerKitCommand#resetAll(ServerCommandSource) all}
     *    │   └──{@link AbstractTinkerKitCommand#reset(ServerCommandSource, Block) &lt;block&gt;}
     *    └── file
     *        ├──{@link AbstractTinkerKitCommand#load(ServerCommandSource) load}
     *        └──{@link AbstractTinkerKitCommand#update(ServerCommandSource) update} </pre>
     */
    public void register(CommandDispatcher<ServerCommandSource> dispatcher, CommandRegistryAccess registryAccess) {
        dispatcher.register(literal(this.ALIAS)
            .requires(source -> CommandHelper.canUseCommand(source, this.getType().rule))
            .then(literal("list").executes(ctx -> this.list(ctx.getSource())))
            .then(literal("get")
                .then(argument("block", RegistryEntryReferenceArgumentType.registryEntry(registryAccess, RegistryKeys.BLOCK))
                    .suggests((ctx, builder) -> suggestMatching(this.getType().getBlockKeys(), builder))
                    .executes(ctx -> this.get(ctx.getSource(),
                        RegistryEntryReferenceArgumentType.getRegistryEntry(ctx, "block", RegistryKeys.BLOCK).value()
                    ))
                )
            )
            .then(literal("set")
                .then(argument("value", this.getArgumentType())
                    .suggests((ctx, builder) -> suggestMatching(this.getSuggestions().map(Objects::toString), builder))
                    .then(literal("all")
                        .executes(ctx -> this.setAll(ctx.getSource(),
                            this.getInputValue(ctx.getArgument("value", this.getObjectClass()))
                        ))
                    )
                    .then(argument("block", RegistryEntryReferenceArgumentType.registryEntry(registryAccess, RegistryKeys.BLOCK))
                        .suggests((ctx, builder) -> suggestMatching(this.getType().getBlockKeys(), builder))
                        .executes(ctx -> this.set(ctx.getSource(),
                            RegistryEntryReferenceArgumentType.getRegistryEntry(ctx, "block", RegistryKeys.BLOCK).value(),
                            this.getInputValue(ctx.getArgument("value", this.getObjectClass()))
                        ))
                    )
                )
            )
            .then(literal("reset")
                .then(literal("all").executes(ctx -> this.resetAll(ctx.getSource())))
                .then(argument("block", RegistryEntryReferenceArgumentType.registryEntry(registryAccess, RegistryKeys.BLOCK))
                    .suggests((ctx, builder) -> suggestMatching(this.getType().getBlockKeys(), builder))
                    .executes(ctx -> this.reset(ctx.getSource(),
                        RegistryEntryReferenceArgumentType.getRegistryEntry(ctx, "block", RegistryKeys.BLOCK).value()
                    ))
                )
            )
            .then(literal("file")
                .requires(source -> DoormatServer.IS_DEBUG)
                .then(literal("load").executes(ctx -> this.load(ctx.getSource())))
                .then(literal("update").executes(ctx -> this.update(ctx.getSource())))
            )
        );
    }

    /**
     * Assigns the inputted value to the entered-in redstone component.
     */
    private int set(ServerCommandSource source, Block block, Object value) {
        if (!this.getType().canModify(block)) {
            Messenger.m(source, String.format("r \"%s\" is not a valid redstone component!", TinkerKit.getTranslatedName(block)));
            return 0;
        }

        if (value == null) {
            Messenger.m(source, String.format("r Invalid %s value!", this.ALIAS));
            return 0;
        }

        if (this.getType().getValue(block) == value) {
            Messenger.m(source, String.format("r %s %s value is already set to %s!", TinkerKit.getTranslatedName(block), this.ALIAS, value));
            return 0;
        }

        this.getType().set(block, value);

        if (!this.updateFile(source))
            return 0;

        Messenger.m(source, String.format("w Set %s %s value to %s", TinkerKit.getTranslatedName(block), this.ALIAS, value));
        return Command.SINGLE_SUCCESS;
    }

    /**
     * Assigns the inputted value to every redstone component in this map.
     */
    private int setAll(ServerCommandSource source, Object value) {
        boolean wasModified = false;

        for (Block block : this.getType().getBlocks()) {
            if (this.getType().getValue(block) == value)
                continue;

            this.getType().set(block, value);
            wasModified = true;
        }

        if (!wasModified) {
            Messenger.m(source, String.format("r All %s values match %s. Try tweaking some settings first!", this.ALIAS, value));
            return 0;
        }

        if (!this.updateFile(source))
            return 0;

        Messenger.m(source, String.format("w Set all %s values to %s", this.ALIAS, value));
        return Command.SINGLE_SUCCESS;
    }

    /**
     * Finds and prints the value assigned to the inputted redstone component for this map to chat.
     */
    private int get(ServerCommandSource source, Block block) {
        if (!this.getType().canModify(block)) {
            Messenger.m(source, String.format("r %s is not a valid redstone component!", TinkerKit.getTranslatedName(block)));
            return 0;
        }

        Object value = this.getType().getValue(block);
        Messenger.m(source, String.format("w %s %s value is set to %s (%s value)", TinkerKit.getTranslatedName(block), this.ALIAS, value, this.getType().isDefaultValue(block) ? "default" : "modified"));

        return Command.SINGLE_SUCCESS;
    }

    /**
     * Finds and prints all values for this map to a list in chat.
     */
    private int list(ServerCommandSource source) {
        Messenger.m(source, "");

        if (this.getType().hasBeenModified()) {
            String cmd = String.format("?/%s reset all", this.ALIAS);
            Messenger.m(source, String.format("bui %s values:", StringUtils.capitalize(this.ALIAS)), cmd, "^g Restore default values?");
        }
        else Messenger.m(source, String.format("bu %s values:", StringUtils.capitalize(this.ALIAS)));

        for (Block block : this.getType().getBlocks()) {
            Object value = this.getType().getValue(block);
            String s = String.format("%s: %s", TinkerKit.getTranslatedName(block), value);

            if (this.getType().isDefaultValue(block))
                Messenger.m(source, String.format("g - %s (default value)", s));
            else {
                String cmd = String.format("?/%s reset %s", this.ALIAS, TinkerKit.getKey(block));
                Messenger.m(source, "w - ", String.format("wi %s (modified value)", s), cmd, "^g Restore default value?");
            }
        }

        return Command.SINGLE_SUCCESS;
    }

    /**
     * Sets the inputted redstone component's value to the default for this map.
     */
    private int reset(ServerCommandSource source, Block block) {
        if (!this.getType().canModify(block)) {
            Messenger.m(source, String.format("r %s is not a valid component!", TinkerKit.getTranslatedName(block)));
            return 0;
        }

        if (this.getType().isDefaultValue(block)) {
            Messenger.m(source, String.format("r %s %s value is already set to default!", TinkerKit.getTranslatedName(block), this.ALIAS));
            return 0;
        }

        this.getType().reset(block);

        if (!this.updateFile(source))
            return 0;

        Messenger.m(source, String.format("w Restored default %s %s value", TinkerKit.getTranslatedName(block), this.ALIAS));
        return Command.SINGLE_SUCCESS;
    }

    /**
     * Sets all values to their defaults for this map.
     */
    private int resetAll(ServerCommandSource source) {
        boolean wasModified = false;

        for (Block block : this.getType().getBlocks()) {
            if (this.getType().isDefaultValue(block))
                continue;

            this.getType().reset(block);
            wasModified = true;
        }

        if (!wasModified) {
            Messenger.m(source, String.format("r %s values are already set to default. Try tweaking some settings first!", StringUtils.capitalize(this.ALIAS)));
            return 0;
        }

        if (!this.updateFile(source))
            return 0;

        Messenger.m(source, String.format("w Restored default %s values", ALIAS));
        return Command.SINGLE_SUCCESS;
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
        if (!this.updateFile(source))
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