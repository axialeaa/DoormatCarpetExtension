package com.axialeaa.doormat.tinker_kit;

import com.mojang.brigadier.arguments.ArgumentType;
import it.unimi.dsi.fastutil.objects.Object2ObjectArrayMap;
import net.minecraft.block.Block;
import net.minecraft.registry.Registries;
import net.minecraft.util.Identifier;
import org.jetbrains.annotations.Nullable;
import java.lang.constant.ConstantDesc;
import java.util.*;
import java.util.function.BiPredicate;
import java.util.stream.Stream;

/**
 * @param <W> serialize
 * @param <R> deserialize
 */
public class TinkerType<W extends ConstantDesc, R> {

    public static final List<TinkerType<?, ?>> TYPES = new ArrayList<>();

    final Map<Block, R> defaultValues = new Object2ObjectArrayMap<>();
    final Map<Block, R> transientValues = new Object2ObjectArrayMap<>();

    /**
     * Defines the name of this Tinker Type. This is used for the command as well as the json field.
     */
    final String name;
    final Class<W> writeClass;
    final Serializer<W, R> serializer;
    public final TinkerTypeCommand<W, R> command;

    public TinkerType(
        String name,
        Class<W> writeClass,

        String commandRule,
        Stream<R> commandSuggestions,
        ArgumentType<W> commandArgumentType,

        Serializer<W, R> serializer
    ) {
        this.name = name;
        this.writeClass = writeClass;
        this.serializer = serializer;
        this.command = new TinkerTypeCommand<>(this, commandRule, commandSuggestions, commandArgumentType);

        TYPES.add(this);
    }

    /**
     * @return true if this Tinker Type can modify the component passed through {@code block}.
     */
    public boolean canModify(Block block) {
        for (BiPredicate<Block, TinkerType<?, ?>> condition : TinkerKitUtils.MODIFICATION_PREDICATES) {
            if (!condition.test(block, this))
                return false;
        }

        return this.defaultValues.containsKey(block);
    }

    /**
     * @return true if the map for this tinker type has been modified.
     */
    public boolean hasBeenModified() {
        for (Block block : this.getBlocks()) {
            if (!this.isDefaultValue(block))
                return true;
        }

        return false;
    }

    /**
     * Converts a list of all modifiable blocks into a list of their keys, sorts them
     * alphabetically, and then re-interprets the blocks from the keys.
     *
     * @return a sorted list of blocks ordered alphabetically by their keys.
     */
    public List<Block> getBlocks() {
        List<String> strings = new ArrayList<>(Registries.BLOCK.stream().filter(this::canModify).map(TinkerKitUtils::getKey).toList());
        Collections.sort(strings);

        Stream<String> stream = Arrays.stream(strings.toArray(String[]::new));
        Stream<Block> mapped = stream.map(key -> Registries.BLOCK.get(Identifier.tryParse(key)));

        return mapped.toList();
    }

    /**
     * Converts a list of all modifiable blocks (by <code>entries</code>) into a list of their keys and sorts them
     * alphabetically.
     *
     * @return a sorted array of all modifiable blocks' keys (by <code>entries</code>), used for command
     * autocompletion.
     */
    public String[] getBlockKeys() {
        List<String> strings = new ArrayList<>(Registries.BLOCK.stream().filter(this::canModify).map(TinkerKitUtils::getKey).toList());
        Collections.sort(strings);

        return strings.toArray(String[]::new);
    }

    private Map<Block, R> getValues() {
        Map<Block, R> map = new Object2ObjectArrayMap<>();

        if (this.defaultValues.isEmpty())
            return map;

        map.putAll(this.defaultValues);

        if (!this.transientValues.isEmpty())
            map.putAll(this.transientValues);

        return map;
    }

    /**
     * @param block the block to get the modified defaultValue of.
     * @return the default value assigned to the <code>block</code>.
     */
    public @Nullable R getValue(Block block) {
        return this.getValues().get(block);
    }

    /**
     * @return true if the map value assigned to this component is the default.
     */
    public boolean isDefaultValue(Block block) {
        return !this.transientValues.containsKey(block);
    }

    public void set(Block block, R value) {
        if (!this.canModify(block))
            throw new IllegalArgumentException("Failed to set %s to a new %s value: %s!".formatted(TinkerKitUtils.getTranslatedName(block), this.name, value));

        this.transientValues.put(block, value);
    }

    public void reset(Block block) {
        this.transientValues.remove(block);
    }

}