package com.axialeaa.doormat.tinker_kit;

import com.axialeaa.doormat.registry.DoormatTinkerTypes;
import net.fabricmc.api.ModInitializer;
import net.minecraft.block.Block;
import net.minecraft.registry.Registries;
import net.minecraft.world.tick.TickPriority;
import org.apache.commons.lang3.function.ToBooleanBiFunction;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public class TinkerKitRegistry {

    /**
     * <strong>Should always be called from {@link ModInitializer#onInitialize()}.</strong>
     * @param block The block to assign the values to.
     * @param qc The quasi-connectivity value the <code>block</code> starts out with by default (usually 0).
     * @param delay The delay value the <code>block</code> starts out with by default (usually 0).
     * @param updateType The update entries value the <code>block</code> starts out with by default.
     * @param tickPriority The tick priority value the <code>block</code> starts out with by default.
     */
    public static void putBlock(@NotNull Block block, @Nullable Integer qc, @Nullable Integer delay, @Nullable UpdateType updateType, @Nullable TickPriority tickPriority) {
        DoormatTinkerTypes.QC.defaultValues.put(block, qc);
        DoormatTinkerTypes.DELAY.defaultValues.put(block, delay);
        DoormatTinkerTypes.UPDATE_TYPE.defaultValues.put(block, updateType);
        DoormatTinkerTypes.TICK_PRIORITY.defaultValues.put(block, tickPriority);
    }

    /**
     * <strong>Should always be called from {@link ModInitializer#onInitialize()}.</strong>
     * @param qc The quasi-connectivity value the <code>blocks</code> start out with by default (usually 0).
     * @param delay The delay value the <code>blocks</code> start out with by default (usually 0).
     * @param updateType The update entries value the <code>blocks</code> start out with by default.
     * @param tickPriority The tick priority value the <code>blocks</code> start out with by default.
     * @param blocks a list of blocks to assign the values to.
     */
    public static void putBlocks(@Nullable Integer qc, @Nullable Integer delay, @Nullable UpdateType updateType, @Nullable TickPriority tickPriority, Block... blocks) {
        if (blocks.length == 0)
            throw new IllegalArgumentException("No blocks found in variable argument list!");

        for (Block block : blocks)
            putBlock(block, qc, delay, updateType, tickPriority);
    }

    /**
     * <strong>Should always be called from {@link ModInitializer#onInitialize()}.</strong>
     * @param blockClass the class of which all child blocks should inherit the following values (eg. DoorBlock.class, 0, 0, UpdateType.SHAPE, null).
     * @param qc The quasi-connectivity value the blocks start out with by default (usually 0).
     * @param delay The delay value the blocks start out with by default (usually 0).
     * @param updateType The update entries value the blocks start out with by default.
     * @param tickPriority The tick priority value the blocks start out with by default.
     */
    public static void putBlocksByClass(Class<? extends Block> blockClass, @Nullable Integer qc, @Nullable Integer delay, @Nullable UpdateType updateType, @Nullable TickPriority tickPriority) {
        for (Block block : getBlocksByClass(blockClass))
            putBlock(block, qc, delay, updateType, tickPriority);
    }

    /**
     * @return a stream of blocks based on whether they're an instance of {@code blockClass}.
     */
    private static List<Block> getBlocksByClass(Class<? extends Block> blockClass) {
        return Registries.BLOCK.stream().filter(blockClass::isInstance).toList();
    }

    public static void putCondition(ToBooleanBiFunction<Block, TinkerType<?, ?>> condition) {
        TinkerType.CONDITIONS.add(condition);
    }

}
