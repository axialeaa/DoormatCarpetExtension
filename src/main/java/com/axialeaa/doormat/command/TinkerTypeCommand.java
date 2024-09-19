package com.axialeaa.doormat.command;

import carpet.utils.CommandHelper;
import carpet.utils.Messenger;
import com.axialeaa.doormat.Doormat;
import com.axialeaa.doormat.registry.DoormatTinkerTypeCommands;
import com.axialeaa.doormat.tinker_kit.ConfigFile;
import com.axialeaa.doormat.tinker_kit.TinkerKitUtils;
import com.axialeaa.doormat.tinker_kit.TinkerType;
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
import java.lang.constant.ConstantDesc;
import java.util.Objects;
import java.util.stream.Stream;
import static net.minecraft.command.CommandSource.suggestMatching;
import static net.minecraft.server.command.CommandManager.argument;
import static net.minecraft.server.command.CommandManager.literal;

/**
 * Defines an instance for a new Tinker Kit command, simplifying the registration process of a new one.
 * @param <W> Write
 */
public class TinkerTypeCommand<W extends ConstantDesc, R> {

    /**
     * The {@link TinkerType} associated with this command. Controls the command aliases, chat outputs and maps, among other things.
     */
    public final TinkerType<W, R> type;
    /**
     * The name of the carpet rule associated with this command.
     */
    public final String rule;
    public final Class<W> writeClass;
    /**
     * A stream of objects defining the autocompletion suggestions for the command.
     */
    public final String[] suggestions;
    /**
     * The argument entries for this command's input value. Should be defined as a new instance of an argument entries class, for example {@link IntegerArgumentType#integer()}.
     */
    public final ArgumentType<W> argumentType;

    private final String ALIAS;

    public TinkerTypeCommand(TinkerType<W, R> type, String rule, Class<W> writeClass, Stream<W> suggestions, ArgumentType<W> argumentType) {
        this.type = type;
        this.rule = rule;
        this.writeClass = writeClass;
        this.suggestions = suggestions.map(Objects::toString).toList().toArray(new String[0]);
        this.argumentType = argumentType;

        this.ALIAS = this.type.name;

        DoormatTinkerTypeCommands.LIST.add(this);
    }

    /**
     *<pre> .
     *└── /{@link TinkerTypeCommand#ALIAS}
     *    ├──{@link TinkerTypeCommand#list(ServerCommandSource) list}
     *    ├── get
     *    │   └──{@link TinkerTypeCommand#get(ServerCommandSource, Block) &lt;block&gt;}
     *    ├── set
     *    │   └── &lt;value&gt;
     *    │       ├──{@link TinkerTypeCommand#setAll(ServerCommandSource, R) all}
     *    │       └──{@link TinkerTypeCommand#set(ServerCommandSource, Block, R) &lt;block&gt;}
     *    ├── reset
     *    │   ├──{@link TinkerTypeCommand#resetAll(ServerCommandSource) all}
     *    │   └──{@link TinkerTypeCommand#reset(ServerCommandSource, Block) &lt;block&gt;}
     *    └── file
     *        ├──{@link TinkerTypeCommand#load(ServerCommandSource) load}
     *        └──{@link TinkerTypeCommand#update(ServerCommandSource) update} </pre>
     */
    public void register(CommandDispatcher<ServerCommandSource> dispatcher, CommandRegistryAccess registryAccess) {
        dispatcher.register(literal(this.ALIAS)
            .requires(source -> CommandHelper.canUseCommand(source, this.rule))
            .then(literal("list").executes(ctx -> this.list(ctx.getSource())))
            .then(literal("get")
                .then(argument("block", RegistryEntryReferenceArgumentType.registryEntry(registryAccess, RegistryKeys.BLOCK))
                    .suggests((ctx, builder) -> suggestMatching(this.type.getBlockKeys(), builder))
                    .executes(ctx -> this.get(ctx.getSource(),
                        RegistryEntryReferenceArgumentType.getRegistryEntry(ctx, "block", RegistryKeys.BLOCK).value()
                    ))
                )
            )
            .then(literal("set")
                .then(argument("value", this.argumentType)
                    .suggests((ctx, builder) -> suggestMatching(this.suggestions, builder))
                    .then(literal("all")
                        .executes(ctx -> this.setAll(
                            ctx.getSource(),
                            this.type.read.apply(ctx.getArgument("value", this.writeClass))
                        ))
                    )
                    .then(argument("block", RegistryEntryReferenceArgumentType.registryEntry(registryAccess, RegistryKeys.BLOCK))
                        .suggests((ctx, builder) -> suggestMatching(this.type.getBlockKeys(), builder))
                        .executes(ctx -> this.set(ctx.getSource(),
                            RegistryEntryReferenceArgumentType.getRegistryEntry(ctx, "block", RegistryKeys.BLOCK).value(),
                            this.type.read.apply(ctx.getArgument("value", this.writeClass))
                        ))
                    )
                )
            )
            .then(literal("reset")
                .then(literal("all").executes(ctx -> this.resetAll(ctx.getSource())))
                .then(argument("block", RegistryEntryReferenceArgumentType.registryEntry(registryAccess, RegistryKeys.BLOCK))
                    .suggests((ctx, builder) -> suggestMatching(this.type.getBlockKeys(), builder))
                    .executes(ctx -> this.reset(ctx.getSource(),
                        RegistryEntryReferenceArgumentType.getRegistryEntry(ctx, "block", RegistryKeys.BLOCK).value()
                    ))
                )
            )
            .then(literal("file")
                .requires(source -> Doormat.IS_DEBUG)
                .then(literal("load").executes(ctx -> this.load(ctx.getSource())))
                .then(literal("update").executes(ctx -> this.update(ctx.getSource())))
            )
        );
    }

    /**
     * Assigns the inputted value to the entered-in redstone component.
     */
    private int set(ServerCommandSource source, Block block, R value) {
        if (!this.type.canModify(block)) {
            Messenger.m(source, "r \"%s\" is not a valid redstone component!".formatted(TinkerKitUtils.getTranslatedName(block)));
            return 0;
        }

        if (value == null) {
            Messenger.m(source, "r Invalid %s value!".formatted(this.ALIAS));
            return 0;
        }

        if (this.type.getValue(block) == value) {
            Messenger.m(source, "r %s %s value is already set to %s!".formatted(TinkerKitUtils.getTranslatedName(block), this.ALIAS, value));
            return 0;
        }

        this.type.set(block, value);

        if (!this.updateFile(source))
            return 0;

        Messenger.m(source, "w Set %s %s value to %s".formatted(TinkerKitUtils.getTranslatedName(block), this.ALIAS, value));
        return Command.SINGLE_SUCCESS;
    }

    /**
     * Assigns the inputted value to every redstone component in this map.
     */
    private int setAll(ServerCommandSource source, R value) {
        boolean wasModified = false;

        for (Block block : this.type.getBlocks()) {
            if (this.type.getValue(block) == value)
                continue;

            this.type.set(block, value);
            wasModified = true;
        }

        if (!wasModified) {
            Messenger.m(source, "r All %s values match %s. Try tweaking some settings first!".formatted(this.ALIAS, value));
            return 0;
        }

        if (!this.updateFile(source))
            return 0;

        Messenger.m(source, "w Set all %s values to %s".formatted(this.ALIAS, value));
        return Command.SINGLE_SUCCESS;
    }

    /**
     * Finds and prints the value assigned to the inputted redstone component for this map to chat.
     */
    private int get(ServerCommandSource source, Block block) {
        if (!this.type.canModify(block)) {
            Messenger.m(source, "r %s is not a valid redstone component!".formatted(TinkerKitUtils.getTranslatedName(block)));
            return 0;
        }

        Object value = this.type.getValue(block);
        Messenger.m(source, "w %s %s value is set to %s (%s value)".formatted(TinkerKitUtils.getTranslatedName(block), this.ALIAS, value, this.type.isDefaultValue(block) ? "default" : "modified"));

        return Command.SINGLE_SUCCESS;
    }

    /**
     * Finds and prints all values for this map to a list in chat.
     */
    private int list(ServerCommandSource source) {
        Messenger.m(source, "");

        if (this.type.hasBeenModified()) {
            String cmd = "?/%s reset all".formatted(this.ALIAS);
            Messenger.m(source, "bui %s values:".formatted(StringUtils.capitalize(this.ALIAS)), cmd, "^g Restore default values?");
        }
        else Messenger.m(source, "bu %s values:".formatted(StringUtils.capitalize(this.ALIAS)));

        for (Block block : this.type.getBlocks()) {
            Object value = this.type.getValue(block);
            String s = "%s: %s".formatted(TinkerKitUtils.getTranslatedName(block), value);

            if (this.type.isDefaultValue(block))
                Messenger.m(source, "g - %s (default value)".formatted(s));
            else {
                String cmd = "?/%s reset %s".formatted(this.ALIAS, TinkerKitUtils.getKey(block));
                Messenger.m(source, "w - ", "wi %s (modified value)".formatted(s), cmd, "^g Restore default value?");
            }
        }

        return Command.SINGLE_SUCCESS;
    }

    /**
     * Sets the inputted redstone component's value to the default for this map.
     */
    private int reset(ServerCommandSource source, Block block) {
        if (!this.type.canModify(block)) {
            Messenger.m(source, "r %s is not a valid component!".formatted(TinkerKitUtils.getTranslatedName(block)));
            return 0;
        }

        if (this.type.isDefaultValue(block)) {
            Messenger.m(source, "r %s %s value is already set to default!".formatted(TinkerKitUtils.getTranslatedName(block), this.ALIAS));
            return 0;
        }

        this.type.reset(block);

        if (!this.updateFile(source))
            return 0;

        Messenger.m(source, "w Restored default %s %s value".formatted(TinkerKitUtils.getTranslatedName(block), this.ALIAS));
        return Command.SINGLE_SUCCESS;
    }

    /**
     * Sets all values to their defaults for this map.
     */
    private int resetAll(ServerCommandSource source) {
        boolean wasModified = false;

        for (Block block : this.type.getBlocks()) {
            if (this.type.isDefaultValue(block))
                continue;

            this.type.reset(block);
            wasModified = true;
        }

        if (!wasModified) {
            Messenger.m(source, "r %s values are already set to default. Try tweaking some settings first!".formatted(StringUtils.capitalize(this.ALIAS)));
            return 0;
        }

        if (!this.updateFile(source))
            return 0;

        Messenger.m(source, "w Restored default %s values".formatted(this.ALIAS));
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