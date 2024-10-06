package com.axialeaa.doormat.tinker_kit;

import net.fabricmc.api.ModInitializer;
import net.minecraft.block.Block;
import net.minecraft.registry.Registries;
import org.jetbrains.annotations.NotNull;
import java.util.List;
import java.util.function.BiPredicate;
import java.util.function.Predicate;
import java.util.stream.Stream;

public class TinkerKitRegistry {

    /**
     * <strong>Should always be called from {@link ModInitializer#onInitialize()}.</strong>
     * @param block The block to assign the values to.
     */
    public static void putBlock(@NotNull Block block, Entry<?>... entries) {
        if (entries.length == 0)
            throw new IllegalArgumentException("No entries found in list!");

        for (Entry<?> entry : entries)
            entry.put(block);
    }

    /**
     * <strong>Should always be called from {@link ModInitializer#onInitialize()}.</strong>
     * @param blocks a list of blocks to assign the values to.
     */
    public static void putBlocks(@NotNull List<Block> blocks, Entry<?>... entries) {
        if (blocks.isEmpty())
            throw new IllegalArgumentException("No blocks found in list!");

        for (Block block : blocks)
            putBlock(block, entries);
    }

    /**
     * <strong>Should always be called from {@link ModInitializer#onInitialize()}.</strong>
     */
    public static void putBlocksByPredicate(Predicate<Block> predicate, Entry<?>... entries) {
        Stream<Block> filtered = Registries.BLOCK.stream().filter(predicate);
        putBlocks(filtered.toList(), entries);
    }

    /**
     * <p>A modification predicate is a boolean condition that must be satisfied in order for a certain block's behaviour to be modified by a {@link TinkerType}. This allows for the modification's availability to change at any time during runtime unlike the predicates we may have used to {@link TinkerKitRegistry#putBlocksByPredicate(Predicate, Entry[]) register a block}.
     *
     * <p>This leaves us with a lot of power to change the way the <b>Tinker Kit</b> operates, and we should use it with caution. It is unwise to create a predicate that completely suppresses the ability to modify a block's behaviour, for example.
     */
    public static void putModificationPredicate(BiPredicate<Block, TinkerType<?, ?>> predicate) {
        TinkerKitUtils.MODIFICATION_PREDICATES.add(predicate);
    }

}
