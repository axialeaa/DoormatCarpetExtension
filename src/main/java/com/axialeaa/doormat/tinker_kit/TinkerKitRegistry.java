package com.axialeaa.doormat.tinker_kit;

import com.axialeaa.doormat.DoormatServer;
import com.axialeaa.doormat.util.UpdateType;
import net.fabricmc.api.ModInitializer;
import net.minecraft.block.Block;
import net.minecraft.registry.Registries;
import org.jetbrains.annotations.ApiStatus;

import java.util.HashMap;
import java.util.Map;
import java.util.stream.Stream;

@ApiStatus.NonExtendable
@ApiStatus.Experimental
public class TinkerKitRegistry {

    /**
     * A hashmap which stores all valid blocks alongside their default quasi-connectivity values. This is put-to in {@link DoormatServer#onInitialize()}, and accommodates other mods doing the same.
     */
    private static final Map<Block, Integer> DEFAULT_QC_VALUES = new HashMap<>();
    /**
     * A hashmap which stores all valid blocks alongside their default update type values. This is put-to in {@link DoormatServer#onInitialize()}, and accommodates other mods doing the same.
     */
    private static final Map<Block, UpdateType> DEFAULT_UPDATE_TYPE_VALUES = new HashMap<>();

    static Map<Block, Integer> getDefaultQCValues() {
        return DEFAULT_QC_VALUES;
    }

    static Map<Block, UpdateType> getDefaultUpdateTypeValues() {
        return DEFAULT_UPDATE_TYPE_VALUES;
    }

    /**
     * <strong>Should always be called from {@link ModInitializer#onInitialize()}.</strong>
     * @param defaultQCValue the quasi-connectivity value the <code>block</code> starts out with by default (usually 0).
     * @param block the block to assign <code>defaultQCValue</code> to.
     */
    public static void putBlock(Block block, Integer defaultQCValue) {
        if (block == null)
            throw new IllegalArgumentException("Failed to map default quasi-connectivity value to null block!");

        if (defaultQCValue < 0)
            throw new IllegalArgumentException("Failed to map out-of-bounds quasi-connectivity value (" + defaultQCValue + ") to " + TinkerKit.getTranslatedName(block) + "!");

        DEFAULT_QC_VALUES.put(block, defaultQCValue);
    }

    /**
     * <strong>Should always be called from {@link ModInitializer#onInitialize()}.</strong>
     * @param defaultUpdateTypeValue the update type value the <code>block</code> starts out with by default.
     * @param block the block to assign <code>defaultUpdateTypeValue</code> to.
     */
    public static void putBlock(Block block, UpdateType defaultUpdateTypeValue) {
        if (block == null)
            throw new IllegalArgumentException("Failed to assign default update type value to null block!");

        if (defaultUpdateTypeValue == null)
            throw new IllegalArgumentException("Failed to map null update type value to " + TinkerKit.getTranslatedName(block) + "!");

        DEFAULT_UPDATE_TYPE_VALUES.put(block, defaultUpdateTypeValue);
    }

    /**
     * <strong>Should always be called from {@link ModInitializer#onInitialize()}.</strong>
     * @param defaultQCValue the quasi-connectivity value the <code>block</code> starts out with by default (usually 0).
     * @param defaultUpdateTypeValue the update type value the <code>block</code> starts out with by default.
     * @param block the block to assign these values to.
     */
    public static void putBlock(Block block, Integer defaultQCValue, UpdateType defaultUpdateTypeValue) {
        putBlock(block, defaultQCValue);
        putBlock(block, defaultUpdateTypeValue);
    }

    /**
     * <strong>Should always be called from {@link ModInitializer#onInitialize()}.</strong>
     * @param defaultQCValue the quasi-connectivity value the <code>blocks</code> start out with by default (usually 0).
     * @param blocks a list of blocks to assign <code>defaultQCValue</code> to.
     */
    public static void putBlocks(Integer defaultQCValue, Block... blocks) {
        if (blocks.length == 0)
            throw new IllegalArgumentException("No blocks found in variable argument list!");

        for (Block block : blocks)
            putBlock(block, defaultQCValue);
    }

    /**
     * <strong>Should always be called from {@link ModInitializer#onInitialize()}.</strong>
     * @param defaultUpdateTypeValue the update type value the <code>blocks</code> start out with by default.
     * @param blocks a list of blocks to assign <code>defaultUpdateTypeValue</code> to.
     */
    public static void putBlocks(UpdateType defaultUpdateTypeValue, Block... blocks) {
        if (blocks.length == 0)
            throw new IllegalArgumentException("No blocks found in variable argument list!");

        for (Block block : blocks)
            putBlock(block, defaultUpdateTypeValue);
    }

    /**
     * <strong>Should always be called from {@link ModInitializer#onInitialize()}.</strong>
     * @param defaultQCValue the quasi-connectivity value the <code>blocks</code> start out with by default (usually 0).
     * @param defaultUpdateTypeValue the update type value the <code>blocks</code> start out with by default.
     * @param blocks a list of blocks to assign these values to.
     */
    public static void putBlocks(Integer defaultQCValue, UpdateType defaultUpdateTypeValue, Block... blocks) {
        putBlocks(defaultQCValue, blocks);
        putBlocks(defaultUpdateTypeValue, blocks);
    }

    /**
     * <strong>Should always be called from {@link ModInitializer#onInitialize()}.</strong>
     * @param blockClass the class of which all child blocks should inherit the following values (eg. DoorBlock.class, 0, UpdateType.SHAPE).
     * @param defaultQCValue the quasi-connectivity value the <code>blocks</code> start out with by default (usually 0).
     */
    public static void putBlocksByClass(Class<? extends Block> blockClass, Integer defaultQCValue) {
        for (Block block : getBlocksByClass(blockClass).toList())
            putBlock(block, defaultQCValue);
    }

    /**
     * <strong>Should always be called from {@link ModInitializer#onInitialize()}.</strong>
     * @param blockClass the class of which all child blocks should inherit the following values (eg. DoorBlock.class, UpdateType.SHAPE).
     * @param defaultUpdateTypeValue the update type value the <code>blocks</code> start out with by default.
     */
    public static void putBlocksByClass(Class<? extends Block> blockClass, UpdateType defaultUpdateTypeValue) {
        for (Block block : getBlocksByClass(blockClass).toList())
            putBlock(block, defaultUpdateTypeValue);
    }

    /**
     * <strong>Should always be called from {@link ModInitializer#onInitialize()}.</strong>
     * @param blockClass the class of which all child blocks should inherit the following values (eg. DoorBlock.class, 0, UpdateType.SHAPE).
     * @param defaultQCValue the quasi-connectivity value the <code>blocks</code> start out with by default (usually 0).
     * @param defaultUpdateTypeValue the update type value the <code>blocks</code> start out with by default.
     */
    public static void putBlocksByClass(Class<? extends Block> blockClass, Integer defaultQCValue, UpdateType defaultUpdateTypeValue) {
        putBlocksByClass(blockClass, defaultQCValue);
        putBlocksByClass(blockClass, defaultUpdateTypeValue);
    }

    /**
     * @return a stream of blocks based on whether they're an instance of {@code blockClass}.
     */
    private static Stream<Block> getBlocksByClass(Class<? extends Block> blockClass) {
        return Registries.BLOCK.stream().filter(blockClass::isInstance);
    }

}