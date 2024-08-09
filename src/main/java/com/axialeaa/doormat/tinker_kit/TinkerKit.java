package com.axialeaa.doormat.tinker_kit;

import carpet.CarpetSettings;
import carpet.utils.Translations;
import com.axialeaa.doormat.DoormatServer;
import com.axialeaa.doormat.DoormatSettings;
import com.axialeaa.doormat.util.UpdateType;
import net.fabricmc.api.ModInitializer;
import net.minecraft.block.*;
import net.minecraft.fluid.FluidState;
import net.minecraft.registry.Registries;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.world.RedstoneView;
import net.minecraft.world.World;
import net.minecraft.world.tick.TickPriority;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.injection.ModifyArg;

import java.util.*;
import java.util.stream.Stream;

public class TinkerKit {

    /**
     * This hashmap is a little different to {@link Registry#DEFAULT_QC_VALUES} as it stores the blocks alongside their dynamic, modified values. While the registry map is put-to in {@link DoormatServer#onInitialize()} and changes only when the game starts up, this can change at any time during gameplay.
     * @implNote This falls back to the values specified in the registry map before amending itself later on in runtime, thanks to {@link ConfigFile#loadFromFile(MinecraftServer)}. This just adds a level of robustness in case the game crashes!
     */
    public static final Map<Block, Object> MODIFIED_QC_VALUES = new HashMap<>();
    /**
     * This hashmap is a little different to {@link Registry#DEFAULT_DELAY_VALUES} as it stores the blocks alongside their dynamic, modified values. While the registry map is put-to in {@link DoormatServer#onInitialize()} and changes only when the game starts up, this can change at any time during gameplay.
     * @implNote This falls back to the values specified in the registry map before amending itself later on in runtime, thanks to {@link ConfigFile#loadFromFile(MinecraftServer)}. This just adds a level of robustness in case the game crashes!
     */
    public static final Map<Block, Object> MODIFIED_DELAY_VALUES = new HashMap<>();
    /**
     * This hashmap is a little different to {@link Registry#DEFAULT_UPDATE_TYPE_VALUES} as it stores the blocks alongside their dynamic, modified values. While the registry map is put-to in {@link DoormatServer#onInitialize()} and changes only when the game starts up, this can change at any time during gameplay.
     * @implNote This falls back to the values specified in the registry map before amending itself later on in runtime, thanks to {@link ConfigFile#loadFromFile(MinecraftServer)}. This just adds a level of robustness in case the game crashes!
     */
    public static final Map<Block, Object> MODIFIED_UPDATE_TYPE_VALUES = new HashMap<>();
    /**
     * This hashmap is a little different to {@link Registry#DEFAULT_TICK_PRIORITY_VALUES} as it stores the blocks alongside their dynamic, modified values. While the registry map is put-to in {@link DoormatServer#onInitialize()} and changes only when the game starts up, this can change at any time during gameplay.
     * @implNote This falls back to the values specified in the registry map before amending itself later on in runtime, thanks to {@link ConfigFile#loadFromFile(MinecraftServer)}. This just adds a level of robustness in case the game crashes!
     */
    public static final Map<Block, Object> MODIFIED_TICK_PRIORITY_VALUES = new HashMap<>();

    static {
        try {
            MODIFIED_QC_VALUES.putAll(Registry.DEFAULT_QC_VALUES);
            MODIFIED_UPDATE_TYPE_VALUES.putAll(Registry.DEFAULT_UPDATE_TYPE_VALUES);
            MODIFIED_TICK_PRIORITY_VALUES.putAll(Registry.DEFAULT_TICK_PRIORITY_VALUES);
            MODIFIED_DELAY_VALUES.putAll(Registry.DEFAULT_DELAY_VALUES);

            DoormatServer.LOGGER.info("Tinker Kit hashmaps received default values!");
        }
        catch (Exception e) {
            throw new RuntimeException("Tinker Kit hashmaps failed to receive default values!", e);
        }
    }

    /**
     * @param block the block to get the key of.
     * @return the identifier of the <code>block</code> as a string.
     */
    public static String getKey(Block block) {
        return Registries.BLOCK.getId(block).toString();
    }

    /**
     * @param block the block to get the translated name of.
     * @return the name of the block translated via the lang file on the server side, thanks to {@link Translations}.
     */
    public static String getTranslatedName(Block block) {
        String key = block.getTranslationKey();
        String namespace = Registries.BLOCK.getId(block).getNamespace();
        String path = String.format("assets/%s/lang/%s.json", namespace, CarpetSettings.language);

        return Translations.getTranslationFromResourcePath(path).get(key);
    }

    public static boolean cannotQC(RedstoneView world, BlockPos pos) {
        return world.isOutOfHeightLimit(pos) || DoormatSettings.qcSuppressor && world.getBlockState(pos).isOf(Blocks.EMERALD_ORE);
    }

    /**
     * @param world the world this method is called in.
     * @param pos the block position this method is called at.
     * @param i the number to add onto the range check (useful for doors).
     * @return true if any of the block positions in the specified range are receiving power, otherwise false.
     */
    public static boolean isReceivingRedstonePower(RedstoneView world, BlockPos pos, BlockState state, int i) {
        Block block = state.getBlock();

        if (Type.QC.canModify(block)) {
            for (int j = 0; j <= (int) Type.QC.getModifiedValue(block) + i; j++) {
                BlockPos blockPos = pos.up(j);

                if (cannotQC(world, blockPos))
                    break;

                if (world.isReceivingRedstonePower(blockPos))
                    return true;
            }
        }

        return world.isReceivingRedstonePower(pos);
    }

    /**
     * @param world the world this method is called in.
     * @param pos the block position this method is called at.
     * @return true if any of the block positions in the specified range are receiving power, otherwise false.
     */
    public static boolean isReceivingRedstonePower(RedstoneView world, BlockPos pos, BlockState state) {
        return isReceivingRedstonePower(world, pos, state, 0);
    }

    /**
     * An alternative implementation of {@link #isReceivingRedstonePower(RedstoneView, BlockPos, BlockState, int)} which takes in a direction--used for redstone torches.
     * <p>
     * This has been re-implemented due to the semantic difference between {@link net.minecraft.world.RedstoneView#isEmittingRedstonePower(BlockPos, Direction) isEmittingRedstonePower()} and {@link net.minecraft.world.RedstoneView#isReceivingRedstonePower(BlockPos) isReceivingRedstonePower()}. The second will return true if any of the blocks adjacent to the block position are powered, whereas the first checks for only the block position itself. This makes sense for torches and really nothing else, which unpower when the block they're resting on is sourcing power.
     * @param world the world this method is called in.
     * @param pos the block position this method is called at.
     * @param direction the direction to check if power is being received.
     * @param i the number to add onto the range check.
     * @return true if any of the block positions in the specified range are receiving power, otherwise false.
     */
    public static boolean isReceivingRedstonePower(RedstoneView world, BlockPos pos, BlockState state, Direction direction, int i) {
        return getEmittedRedstonePower(world, pos, state, direction, i) > 0;
    }

    /**
     * An alternative implementation of {@link #isReceivingRedstonePower(RedstoneView, BlockPos, BlockState)} which takes in a direction--used for redstone torches.
     * <p>
     * This has been re-implemented due to the semantic difference between {@link net.minecraft.world.RedstoneView#isEmittingRedstonePower(BlockPos, Direction) isEmittingRedstonePower()} and {@link net.minecraft.world.RedstoneView#isReceivingRedstonePower(BlockPos) isReceivingRedstonePower()}. The second will return true if any of the blocks adjacent to the block position are powered, whereas the first checks for only the block position itself. This makes sense for torches and really nothing else, which unpower when the block they're resting on is sourcing power.
     * @param world the world this method is called in.
     * @param pos the block position this method is called at.
     * @param direction the direction to check if power is being received.
     * @return true if any of the block positions in the specified range are receiving power, otherwise false.
     */
    public static boolean isReceivingRedstonePower(RedstoneView world, BlockPos pos, BlockState state, Direction direction) {
        return getEmittedRedstonePower(world, pos, state, direction, 0) > 0;
    }

    /**
     * An alternative implementation of {@link #isReceivingRedstonePower(RedstoneView, BlockPos, BlockState, int)} which instead outputs a signal strength--used for diodes.
     * <p>
     * This has been re-implemented due to diodes ({@link AbstractRedstoneGateBlock}<code>s</code>) taking in integer inputs instead of booleans like most other components; they byFlags told <i>if</i> they're powered, not to "what degree".
     * @param world the world this method is called in.
     * @param pos the block position this method is called at.
     * @param direction the direction to check if power is being received.
     * @param i the number to add onto the range check.
     * @return the largest signal strength within the quasi-connectivity range.
     */
    public static int getEmittedRedstonePower(RedstoneView world, BlockPos pos, BlockState state, Direction direction, int i) {
        Block block = state.getBlock();

        if (Type.QC.canModify(block)) {
            int power = 0;

            for (int j = 0; j <= (int) Type.QC.getModifiedValue(block) + i; j++) {
                BlockPos blockPos = pos.offset(direction).up(j);

                if (cannotQC(world, blockPos) || power >= 15)
                    break;

                power = Math.max(power, world.getEmittedRedstonePower(blockPos, direction));
            }

            return power;
        }

        return world.getEmittedRedstonePower(pos, direction);
    }

    /**
     * An alternative implementation of {@link #isReceivingRedstonePower(RedstoneView, BlockPos, BlockState)} which instead outputs a signal strength--used for diodes.
     * <p>
     * This has been re-implemented due to diodes ({@link AbstractRedstoneGateBlock}<code>s</code>) taking in integer inputs instead of booleans like most other components; they byFlags told <i>if</i> they're powered, not to "what degree".
     * @param world the world this method is called in.
     * @param pos the block position this method is called at.
     * @param direction the direction to check if power is being received.
     * @return the largest signal strength within the quasi-connectivity range.
     */
    public static int getEmittedRedstonePower(RedstoneView world, BlockPos pos, BlockState state, Direction direction) {
        return getEmittedRedstonePower(world, pos, state, direction, 0);
    }

    /**
     * @param state the blockstate this method checks for.
     * @param fallback the default delay this method should fall back to if it can't find the default value in the registry map.
     * @return the delay for the given blockstate.
     */
    public static int getDelay(BlockState state, int fallback) {
        Block block = state.getBlock();

        if (Type.DELAY.canModify(block))
            return (int) Type.DELAY.getModifiedValue(block);

        return fallback;
    }

    /**
     * @param state the blockstate this method checks for.
     * @param fallback the default flags this method should fall back to if it can't find the default value in the registry map (should be the third parameter in the call to {@link World#setBlockState(BlockPos, BlockState, int)}, obtained by default when using {@link ModifyArg} to index 2).
     * @return the update type flag(s) for the given blockstate.
     */
    public static int getFlags(BlockState state, int fallback) {
        Block block = state.getBlock();
        
        if (Type.UPDATE_TYPE.canModify(block))
            return ((UpdateType) Type.UPDATE_TYPE.getModifiedValue(block)).flags;

        return fallback;
    }

    /**
     * An alternative implementation of {@link World#removeBlock(BlockPos, boolean)}, allowing for custom update flags based on the block passed through {@code state}.
     * @param world The world this method is called in.
     * @param pos The block position this method is called at.
     * @param state The block state this method is called on.
     * @param move Whether the block is moving.
     */
    public static boolean removeBlock(World world, BlockPos pos, BlockState state, boolean move) {
        FluidState fluidState = world.getFluidState(pos);
        return world.setBlockState(pos, fluidState.getBlockState(), getFlags(state, Block.NOTIFY_ALL) | (move ? Block.MOVED : 0));
    }

    /**
     * @param state the blockstate this method checks for.
     * @param fallback the default flags this method should fall back to if it can't find the default value in the registry map (should be the third parameter in the call to {@link World#setBlockState(BlockPos, BlockState, int)}, obtained by default when using {@link ModifyArg} to index 2).
     * @return true if the update type flags of the given blockstate are odd (BLOCK, 1 and BOTH, 3).
     */
    public static boolean shouldUpdateNeighbours(BlockState state, int fallback) {
        return (getFlags(state, fallback) & Block.NOTIFY_NEIGHBORS) == 1;
    }

    /**
     * An alternative implementation of {@link TinkerKit#shouldUpdateNeighbours(BlockState, int)} which assumes a default fallback value equivalent to {@link Block#NOTIFY_ALL}.
     * @param state the blockstate this method checks for.
     * @return true if the update type flags of the given blockstate are odd (BLOCK, 1 and BOTH, 3).
     */
    public static boolean shouldUpdateNeighbours(BlockState state) {
        return shouldUpdateNeighbours(state, Block.NOTIFY_ALL);
    }

    /**
     * @param state the blockstate this method checks for.
     * @param fallback the default tick priority this method should fall back to if it can't find the default value in the registry map.
     * @return the tick priority for the given blockstate.
     */
    public static TickPriority getTickPriority(BlockState state, TickPriority fallback) {
        Block block = state.getBlock();

        if (!Type.TICK_PRIORITY.canModify(block))
            return (TickPriority) Type.TICK_PRIORITY.getModifiedValue(block);

        return fallback;
    }

    /**
     * Used for multiple instances of needing to specify which type of redstone rule to modify via commands or the config file.
     */
    public enum Type {

        QC            ("quasiconnectivity", DoormatSettings.commandQC, Registry.DEFAULT_QC_VALUES, MODIFIED_QC_VALUES),
        DELAY         ("delay", DoormatSettings.commandDelay, Registry.DEFAULT_DELAY_VALUES, MODIFIED_DELAY_VALUES),
        UPDATE_TYPE   ("updatetype", DoormatSettings.commandUpdateType, Registry.DEFAULT_UPDATE_TYPE_VALUES, MODIFIED_UPDATE_TYPE_VALUES),
        TICK_PRIORITY ("tickpriority", DoormatSettings.commandTickPriority, Registry.DEFAULT_TICK_PRIORITY_VALUES, MODIFIED_TICK_PRIORITY_VALUES);

        public final String name;
        public final String commandRule;
        private final Map<Block, Object> defaultValues;
        private final Map<Block, Object> modifiedValues;

        Type(String name, String commandRule, Map<Block, Object> defaultValues, Map<Block, Object> modifiedValues) {
            this.name = name;
            this.commandRule = commandRule;
            this.defaultValues = defaultValues;
            this.modifiedValues = modifiedValues;
        }

        /**
         * @return true if this rule type has support for the component.
         */
        public boolean canModify(Block block) {
            if (!DoormatSettings.redstoneOpensBarrels && block instanceof BarrelBlock)
                return false;

            if (Type.DELAY.modifiedValues.containsKey(block) && (int) Type.DELAY.modifiedValues.get(block) == 0 && this == Type.TICK_PRIORITY)
                return false;

            return this.defaultValues.containsKey(block);
        }

        /**
         * @return true if the map for this rule type has been modified.
         */
        public boolean isMapModified() {
            for (Block block : this.getBlocks().toList()) {
                if (!this.isDefaultValue(block))
                    return true;
            }

            return false;
        }

        /**
         * Converts a list of all modifiable blocks (by <code>type</code>) into a list of their keys, sorts them alphabetically, and then re-interprets the blocks from the keys.
         * @return a sorted stream of blocks ordered alphabetically by their keys.
         */
        public Stream<Block> getBlocks() {
            List<String> strings = new ArrayList<>(Registries.BLOCK.stream().filter(this::canModify).map(TinkerKit::getKey).toList());
            Collections.sort(strings);

            return Arrays.stream(strings.toArray(String[]::new)).map(key -> Registries.BLOCK.get(Identifier.tryParse(key)));
        }

        /**
         * Converts a list of all modifiable blocks (by <code>type</code>) into a list of their keys and sorts them alphabetically.
         * @return a sorted array of all modifiable blocks' keys (by <code>type</code>), used for command autocompletion.
         */
        public String[] getBlockKeys() {
            List<String> strings = new ArrayList<>(Registries.BLOCK.stream().filter(this::canModify).map(TinkerKit::getKey).toList());
            Collections.sort(strings);

            return strings.toArray(String[]::new);
        }

        /**
         * @param block the block to get the default value of.
         * @return the default value assigned to the <code>block</code>.
         * @throws NullPointerException if no value can be found for the <code>block</code>.
         * @apiNote This requires you cast the return value depending on the {@link Type}.
         */
        public Object getDefaultValue(Block block) {
            if (!this.defaultValues.containsKey(block))
                throw new NullPointerException(String.format("Failed to find the default %s value for %s!", this.name, getTranslatedName(block)));

            return this.defaultValues.get(block);
        }

        /**
         * @param block the block to get the modified value of.
         * @return the default value assigned to the <code>block</code>.
         * @throws NullPointerException if no value can be found for the <code>block</code>.
         * @apiNote This requires you cast the return value depending on the {@link Type}.
         */
        public Object getModifiedValue(Block block) {
            if (!this.modifiedValues.containsKey(block))
                throw new NullPointerException(String.format("Failed to find the modified %s value for %s!", this.name, getTranslatedName(block)));

            return this.modifiedValues.get(block);
        }

        /**
         * @return true if the map value assigned to this component is the default.
         */
        public boolean isDefaultValue(Block block) {
            return this.getModifiedValue(block) == this.getDefaultValue(block);
        }

        private boolean trySet(Block block, Object value) {
            if (this.canModify(block)) {
                try {
                    this.modifiedValues.put(block, value);
                    return true;
                }
                catch (Exception ignored) {}
            }

            return false;
        }

        public void set(Block block, Object value) {
            if (!trySet(block, value))
                throw new IllegalArgumentException(String.format("Failed to set %s to a new %s value: %s!", getTranslatedName(block), this.name, value));
        }

        public void reset(Block block) {
            this.set(block, this.getDefaultValue(block));
        }

    }

    public static class Registry {

        /**
         * A hashmap which stores all valid blocks alongside their default quasi-connectivity values. This is put-to in {@link DoormatServer#onInitialize()}, and accommodates other mods doing the same.
         */
        static final Map<Block, Object> DEFAULT_QC_VALUES = new HashMap<>();
        /**
         * A hashmap which stores all valid blocks alongside their default delay values. This is put-to in {@link DoormatServer#onInitialize()}, and accommodates other mods doing the same.
         */
        static final Map<Block, Object> DEFAULT_DELAY_VALUES = new HashMap<>();
        /**
         * A hashmap which stores all valid blocks alongside their default update type values. This is put-to in {@link DoormatServer#onInitialize()}, and accommodates other mods doing the same.
         */
        static final Map<Block, Object> DEFAULT_UPDATE_TYPE_VALUES = new HashMap<>();
        /**
         * A hashmap which stores all valid blocks alongside their default tick priority values. This is put-to in {@link DoormatServer#onInitialize()}, and accommodates other mods doing the same.
         */
        static final Map<Block, Object> DEFAULT_TICK_PRIORITY_VALUES = new HashMap<>();

        /**
         * <strong>Should always be called from {@link ModInitializer#onInitialize()}.</strong>
         * @param block The block to assign the values to.
         * @param defaultQCValue The quasi-connectivity value the <code>block</code> starts out with by default (usually 0).
         * @param defaultDelayValue The delay value the <code>block</code> starts out with by default (usually 0).
         * @param defaultUpdateTypeValue The update type value the <code>block</code> starts out with by default.
         * @param defaultTickPriorityValue The tick priority value the <code>block</code> starts out with by default.
         */
        public static void putBlock(Block block, @Nullable Integer defaultQCValue, @Nullable Integer defaultDelayValue, @Nullable UpdateType defaultUpdateTypeValue, @Nullable TickPriority defaultTickPriorityValue) {
            if (block == null)
                throw new IllegalArgumentException(String.format("Failed to map default Tinker Kit values (quasiconnectivity: %s, delay: %s, updatetype: %s, tickpriority: %s) to null block!", defaultQCValue, defaultDelayValue, defaultUpdateTypeValue, defaultTickPriorityValue));

            if (defaultQCValue != null) {
                if (defaultQCValue < 0)
                    throw new IllegalArgumentException(String.format("Failed to map out-of-bounds quasi-connectivity value (%s) to block %s!", defaultQCValue, block));

                DEFAULT_QC_VALUES.put(block, defaultQCValue);
            }

            if (defaultDelayValue != null) {
                if (defaultDelayValue < 0)
                    throw new IllegalArgumentException(String.format("Failed to map out-of-bounds delay value (%s) to block %s!", defaultDelayValue, block));

                DEFAULT_DELAY_VALUES.put(block, defaultDelayValue);
            }

            if (defaultUpdateTypeValue != null)
                DEFAULT_UPDATE_TYPE_VALUES.put(block, defaultUpdateTypeValue);

            if (defaultTickPriorityValue != null)
                DEFAULT_TICK_PRIORITY_VALUES.put(block, defaultTickPriorityValue.getIndex());
        }

        /**
         * <strong>Should always be called from {@link ModInitializer#onInitialize()}.</strong>
         * @param defaultQCValue The quasi-connectivity value the <code>blocks</code> start out with by default (usually 0).
         * @param defaultDelayValue The delay value the <code>blocks</code> start out with by default (usually 0).
         * @param defaultUpdateTypeValue The update type value the <code>blocks</code> start out with by default.
         * @param defaultTickPriorityValue The tick priority value the <code>blocks</code> start out with by default.
         * @param blocks a list of blocks to assign the values to.
         */
        public static void putBlocks(@Nullable Integer defaultQCValue, @Nullable Integer defaultDelayValue, @Nullable UpdateType defaultUpdateTypeValue, @Nullable TickPriority defaultTickPriorityValue, Block... blocks) {
            if (blocks.length == 0)
                throw new IllegalArgumentException("No blocks found in variable argument list!");

            for (Block block : blocks)
                putBlock(block, defaultQCValue, defaultDelayValue, defaultUpdateTypeValue, defaultTickPriorityValue);
        }

        /**
         * <strong>Should always be called from {@link ModInitializer#onInitialize()}.</strong>
         * @param blockClass the class of which all child blocks should inherit the following values (eg. DoorBlock.class, 0, 0, UpdateType.SHAPE, null).
         * @param defaultQCValue The quasi-connectivity value the blocks start out with by default (usually 0).
         * @param defaultDelayValue The delay value the blocks start out with by default (usually 0).
         * @param defaultUpdateTypeValue The update type value the blocks start out with by default.
         * @param defaultTickPriorityValue The tick priority value the blocks start out with by default.
         */
        public static void putBlocksByClass(Class<? extends Block> blockClass, @Nullable Integer defaultQCValue, @Nullable Integer defaultDelayValue, @Nullable UpdateType defaultUpdateTypeValue, @Nullable TickPriority defaultTickPriorityValue) {
            for (Block block : getBlocksByClass(blockClass).toList())
                putBlock(block, defaultQCValue, defaultDelayValue, defaultUpdateTypeValue, defaultTickPriorityValue);
        }

        /**
         * @return a stream of blocks based on whether they're an instance of {@code blockClass}.
         */
        private static Stream<Block> getBlocksByClass(Class<? extends Block> blockClass) {
            return Registries.BLOCK.stream().filter(blockClass::isInstance);
        }

    }

    private record Entry(@Nullable Integer qc, @Nullable UpdateType updateType, @Nullable Integer delay, @Nullable TickPriority tickPriority) {}

}