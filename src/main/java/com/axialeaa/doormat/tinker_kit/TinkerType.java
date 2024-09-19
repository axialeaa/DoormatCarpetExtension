package com.axialeaa.doormat.tinker_kit;

import com.axialeaa.doormat.registry.DoormatTinkerTypes;
import com.google.gson.JsonElement;
import com.google.gson.JsonPrimitive;
import net.minecraft.block.Block;
import net.minecraft.registry.Registries;
import net.minecraft.util.Identifier;
import org.apache.commons.lang3.function.ToBooleanBiFunction;
import org.jetbrains.annotations.Nullable;
import java.lang.constant.ConstantDesc;
import java.util.*;
import java.util.function.Function;
import java.util.stream.Stream;

/**
 * @param <W> write
 * @param <R> read
 */
public class TinkerType<W extends ConstantDesc, R> {

    static final List<ToBooleanBiFunction<Block, TinkerType<?, ?>>> CONDITIONS = new ArrayList<>();

    final Map<Block, R> defaultValues = new HashMap<>();
    final Map<Block, R> transientValues = new HashMap<>();

    /**
     * Defines the name of this Tinker Type. This is used for the command as well as the json field.
     */
    public final String name;
    /**
     * Defines a pathway from the readable value (stored in the hashmaps) and a writable value (stored in the json file). This allows us to save the values from the transient map into a format json can parse.
     */
    public final Function<R, W> write;
    /**
     * Defines a pathway from the writable value (stored in the json file) and a readable value (stored in the hashmaps). This allows us to extract the values from the json file, "jiggle them around" a bit, and save them to the transient map in a way that can be parsed by the game without any expensive and repetitive conversions.
     */
    public final Function<W, R> read;
    /**
     * Defines a pathway from a {@link JsonElement} to a writable value that can be operated on.
     */
    public final Function<JsonElement, W> parseJsonFunction;
    /**
     * Defines a pathway from a writable value to a {@link JsonPrimitive} that can be parsed by the config writer.
     */
    public final Function<W, JsonPrimitive> jsonParse;

    public TinkerType(String name, Function<R, W> write, Function<W, R> read, Function<JsonElement, W> parseJsonFunction, Function<W, JsonPrimitive> jsonParse) {
        this.name = name;
        this.write = write;
        this.read = read;

        this.parseJsonFunction = parseJsonFunction;
        this.jsonParse = jsonParse;

        DoormatTinkerTypes.LIST.add(this);
    }

    /**
     * @return true if this tinker type can modify the component passed through {@code block}.
     */
    public boolean canModify(Block block) {
        for (ToBooleanBiFunction<Block, TinkerType<?, ?>> condition : CONDITIONS) {
            if (!condition.applyAsBoolean(block, this))
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
     * Converts a list of all modifiable blocks (by <code>entries</code>) into a list of their keys, sorts them
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
        Map<Block, R> map = new HashMap<>();

        if (!this.defaultValues.isEmpty())
            map.putAll(this.defaultValues);

        if (!this.transientValues.isEmpty())
            map.putAll(this.transientValues);

        return map;
    }

    /**
     * @param block the block to get the modified value of.
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

        this.transientValues.putIfAbsent(block, value);
    }

    public void reset(Block block) {
        this.transientValues.remove(block);
    }

    public @Nullable R getRFromElement(JsonElement element) {
        W value = this.parseJsonFunction.apply(element);
        return this.read.apply(value);
    }

    public JsonPrimitive getPrimitiveFromR(R value) {
        W applied = write.apply(value);
        return this.jsonParse.apply(applied);
    }

}