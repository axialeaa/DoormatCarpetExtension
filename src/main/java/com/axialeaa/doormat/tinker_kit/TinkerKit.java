package com.axialeaa.doormat.tinker_kit;

import carpet.CarpetSettings;
import carpet.utils.Translations;
import com.axialeaa.doormat.DoormatServer;
import com.axialeaa.doormat.settings.DoormatSettings;
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
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.injection.ModifyArg;
import java.util.*;
import java.util.stream.Stream;

public class TinkerKit {

    /**
     * A hashmap which stores all valid blocks alongside their default values. This is put-to in {@link DoormatServer#onInitialize()}, and accommodates other mods doing the same.
     */
    static final Map<Type, Map<Block, Object>> DEFAULT_VALUES = new HashMap<>();
    /**
     * This hashmap is a little different to {@link TinkerKit#DEFAULT_VALUES} as it stores the blocks alongside their dynamic, modified values. While the registry map is put-to in {@link DoormatServer#onInitialize()} and changes only when the game starts up, this can change at any time during gameplay.
     * @implNote This falls back to the values specified in the registry map before amending itself later on in runtime, thanks to {@link ConfigFile#loadFromFile(MinecraftServer)}. This just adds a level of robustness in case the game crashes!
     */
    static final Map<Type, Map<Block, Object>> TRANSIENT_VALUES = new HashMap<>();

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
        String path = "assets/%s/lang/%s.json".formatted(namespace, CarpetSettings.language);

        return Translations.getTranslationFromResourcePath(path).get(key);
    }

    public static boolean cannotQC(RedstoneView world, BlockPos pos) {
        return world.isOutOfHeightLimit(pos) || DoormatSettings.qcSuppressor && world.getBlockState(pos).isOf(Blocks.EMERALD_ORE);
    }

    /**
     * Safely adds a new entry to a Tinker Kit hashmap of type {@code Map<Type, Map<Block, Object>>}, preventing {@link UnsupportedOperationException} throws when trying to call {@link Map#put(Object, Object)}.
     */
    private static void putSafelyWithType(Map<Type, Map<Block, Object>> map, @NotNull Type type, @NotNull Block block, @NotNull Object value) {
        if (map == null)
            return;

        if (!map.containsKey(type)) {
            map.put(type, new HashMap<>(Map.of(block, value)));
            return;
        }

        putSafely(map.get(type), block, value);
    }

    /**
     * Safely adds a new entry to a Tinker Kit hashmap of type {@code Map<Block, Object>}, preventing {@link UnsupportedOperationException} throws when trying to call {@link Map#put(Object, Object)}.
     */
    private static void putSafely(Map<Block, Object> map, @NotNull Block block, @NotNull Object value) {
        if (map == null)
            return;

        Map<Block, Object> newMap = new HashMap<>(map);
        newMap.put(block, value);

        map.putAll(newMap);
    }

    /**
     * @param world the world this method is called in.
     * @param pos the block position this method is called at.
     * @param i the number to add onto the range check (useful for doors).
     * @return true if any of the block positions in the specified range are receiving power, otherwise false.
     */
    public static boolean isReceivingRedstonePower(RedstoneView world, BlockPos pos, Block block, int i) {
        if (!Type.QC.canModify(block))
            return world.isReceivingRedstonePower(pos);

        var value = Type.QC.getValue(block);

        if (value == null)
            return world.isReceivingRedstonePower(pos);

        for (int j = 0; j <= (int) value + i; j++) {
            BlockPos blockPos = pos.up(j);

            if (cannotQC(world, blockPos))
                break;

            if (world.isReceivingRedstonePower(blockPos))
                return true;
        }

        return world.isReceivingRedstonePower(pos);
    }

    /**
     * @param world the world this method is called in.
     * @param pos the block position this method is called at.
     * @return true if any of the block positions in the specified range are receiving power, otherwise false.
     */
    public static boolean isReceivingRedstonePower(RedstoneView world, BlockPos pos, Block block) {
        return isReceivingRedstonePower(world, pos, block, 0);
    }

    /**
     * An alternative implementation of {@link #isReceivingRedstonePower(RedstoneView, BlockPos, Block, int)} which takes in a direction--used for redstone torches.
     * <p>
     * This has been re-implemented due to the semantic difference between {@link RedstoneView#isEmittingRedstonePower(BlockPos, Direction) isEmittingRedstonePower()} and {@link RedstoneView#isReceivingRedstonePower(BlockPos) isEmittingRedstonePower()}. The second will return true if any of the blocks adjacent to the block position are powered, whereas the first checks for only the block position itself. This makes sense for torches and really nothing else, which unpower when the block they're resting on is sourcing power.
     * @param world the world this method is called in.
     * @param pos the block position this method is called at.
     * @param direction the direction to check if power is being received.
     * @param i the number to add onto the range check.
     * @return true if any of the block positions in the specified range are receiving power, otherwise false.
     */
    public static boolean isEmittingRedstonePower(RedstoneView world, BlockPos pos, Block block, Direction direction, int i) {
        return getEmittedRedstonePower(world, pos, block, direction, i) > 0;
    }

    /**
     * An alternative implementation of {@link #isReceivingRedstonePower(RedstoneView, BlockPos, Block)} which takes in a direction--used for redstone torches.
     * <p>
     * This has been re-implemented due to the semantic difference between {@link RedstoneView#isEmittingRedstonePower(BlockPos, Direction) isEmittingRedstonePower()} and {@link RedstoneView#isReceivingRedstonePower(BlockPos) isEmittingRedstonePower()}. The second will return true if any of the blocks adjacent to the block position are powered, whereas the first checks for only the block position itself. This makes sense for torches and really nothing else, which unpower when the block they're resting on is sourcing power.
     * @param world the world this method is called in.
     * @param pos the block position this method is called at.
     * @param direction the direction to check if power is being received.
     * @return true if any of the block positions in the specified range are receiving power, otherwise false.
     */
    public static boolean isEmittingRedstonePower(RedstoneView world, BlockPos pos, Block block, Direction direction) {
        return getEmittedRedstonePower(world, pos, block, direction, 0) > 0;
    }

    /**
     * An alternative implementation of {@link #isReceivingRedstonePower(RedstoneView, BlockPos, Block, int)} which instead outputs a signal strength--used for diodes.
     * <p>
     * This has been re-implemented due to diodes ({@link AbstractRedstoneGateBlock}<code>s</code>) taking in integer inputs instead of booleans like most other components; they byFlags told <i>if</i> they're powered, not to "what degree".
     * @param world the world this method is called in.
     * @param pos the block position this method is called at.
     * @param direction the direction to check if power is being received.
     * @param i the number to add onto the range check.
     * @return the largest signal strength within the quasi-connectivity range.
     */
    public static int getEmittedRedstonePower(RedstoneView world, BlockPos pos, Block block, Direction direction, int i) {
        int power = world.getEmittedRedstonePower(pos, direction);

        if (!Type.QC.canModify(block))
            return power;

        var value = Type.QC.getValue(block);

        if (value == null)
            return power;

        for (int j = 0; j <= (int) value + i; j++) {
            BlockPos blockPos = pos.up(j);

            if (cannotQC(world, blockPos) || power >= 15)
                break;

            power = Math.max(power, world.getEmittedRedstonePower(blockPos, direction));
        }

        return power;
    }

    /**
     * An alternative implementation of {@link #isReceivingRedstonePower(RedstoneView, BlockPos, Block)} which instead outputs a signal strength--used for diodes.
     * <p>
     * This has been re-implemented due to diodes ({@link AbstractRedstoneGateBlock}<code>s</code>) taking in integer inputs instead of booleans like most other components; they byFlags told <i>if</i> they're powered, not to "what degree".
     * @param world the world this method is called in.
     * @param pos the block position this method is called at.
     * @param direction the direction to check if power is being received.
     * @return the largest signal strength within the quasi-connectivity range.
     */
    public static int getEmittedRedstonePower(RedstoneView world, BlockPos pos, Block block, Direction direction) {
        return getEmittedRedstonePower(world, pos, block, direction, 0);
    }

    /**
     * @param block the block this method checks for.
     * @param fallback the default delay this method should fall back to if it can't find the default value in the registry map.
     * @return the delay for the given blockstate.
     */
    public static int getDelay(Block block, int fallback) {
        if (!Type.DELAY.canModify(block))
            return fallback;

        var value = Type.DELAY.getValue(block);

        if (value == null)
            return fallback;

        return (int) value;
    }

    /**
     * @param block the block this method checks for.
     * @param fallback the default flags this method should fall back to if it can't find the default value in the registry map (should be the third parameter in the call to {@link World#setBlockState(BlockPos, BlockState, int)}, obtained by default when using {@link ModifyArg} to index 2).
     * @return the update entries flag(s) for the given blockstate.
     */
    public static int getFlags(Block block, int fallback) {
        if (!Type.UPDATE_TYPE.canModify(block))
            return fallback;

        var value = Type.UPDATE_TYPE.getValue(block);

        if (value == null)
            return fallback;

        return ((UpdateType) value).flags;
    }

    /**
     * Alternative implementation of {@link TinkerKit#getFlags(Block, int)} that has the ability to set certain bits to 0, effectively bypassing certain types of update regardless of the input.
     */
    public static int getFlagsWithRemoved(Block block, int fallback, int removed) {
        return getFlags(block, fallback) & ~removed;
    }

    /**
     * Alternative implementation of {@link TinkerKit#getFlagsWithRemoved(Block, int, int)} that takes in an {@link UpdateType} object instead of an integer.
     */
    public static int getFlagsWithRemoved(Block block, int fallback, UpdateType updateType) {
        return getFlagsWithRemoved(block, fallback, updateType.flags);
    }

    /**
     * Alternative implementation of {@link TinkerKit#getFlagsWithRemoved(Block, int, int)} that removes neighbor updates.
     */
    public static int getFlagsWithoutNeighborUpdate(Block block, int fallback) {
        return getFlagsWithRemoved(block, fallback, UpdateType.BLOCK);
    }

    /**
     * An alternative implementation of {@link World#removeBlock(BlockPos, boolean)}, allowing for custom update flags based on the block passed through {@code state}.
     * @param world The world this method is called in.
     * @param pos The block position this method is called at.
     * @param block The block this method is called on.
     * @param move Whether the block is moving.
     */
    public static boolean removeBlock(World world, BlockPos pos, Block block, boolean move) {
        FluidState fluidState = world.getFluidState(pos);
        return world.setBlockState(pos, fluidState.getBlockState(), getFlags(block, Block.NOTIFY_ALL) | (move ? Block.MOVED : 0));
    }

    /**
     * @param block the block this method checks for.
     * @param fallback the default flags this method should fall back to if it can't find the default value in the registry map (should be the third parameter in the call to {@link World#setBlockState(BlockPos, BlockState, int)}, obtained by default when using {@link ModifyArg} to index 2).
     * @return true if the update entries flags of the given blockstate are odd (BLOCK, 1 and BOTH, 3).
     */
    public static boolean shouldUpdateNeighbours(Block block, int fallback) {
        return (getFlags(block, fallback) & Block.NOTIFY_NEIGHBORS) == Block.NOTIFY_NEIGHBORS;
    }

    /**
     * An alternative implementation of {@link TinkerKit#shouldUpdateNeighbours(Block, int)} which assumes a default fallback value equivalent to {@link Block#NOTIFY_ALL}.
     * @param block the block this method checks for.
     * @return true if the update entries flags of the given blockstate are odd (BLOCK, 1 and BOTH, 3).
     */
    public static boolean shouldUpdateNeighbours(Block block) {
        return shouldUpdateNeighbours(block, Block.NOTIFY_ALL);
    }

    /**
     * @param block the block this method checks for.
     * @param fallback the default tick priority this method should fall back to if it can't find the default value in the registry map.
     * @return the tick priority for the given blockstate.
     */
    public static TickPriority getTickPriority(Block block, TickPriority fallback) {
        if (!Type.TICK_PRIORITY.canModify(block))
            return fallback;

        return (TickPriority) Type.TICK_PRIORITY.getValue(block);
    }

    public static TickPriority getTickPriority(Block block) {
        return getTickPriority(block, TickPriority.NORMAL);
    }

    public enum Type {

        QC            ("quasiconnectivity", DoormatSettings.commandQC),
        DELAY         ("delay", DoormatSettings.commandDelay),
        UPDATE_TYPE   ("updatetype", DoormatSettings.commandUpdateType),
        TICK_PRIORITY ("tickpriority", DoormatSettings.commandTickPriority);

        public final String name;
        public final String rule;

        Type(String name, String rule) {
            this.name = name;
            this.rule = rule;
        }

        public Map<Block, Object> getDefaultMap() {
            return DEFAULT_VALUES.get(this);
        }

        private Map<Block, Object> getTransientMap() {
            return TRANSIENT_VALUES.get(this);
        }

        /**
         * @return true if this rule entries has support for the component.
         */
        public boolean canModify(Block block) {
            if (isBarrelUnmodifiable(block) || (this == Type.TICK_PRIORITY && !hasDelay(block)))
                return false;

            return DEFAULT_VALUES.containsKey(this) && this.getDefaultMap().containsKey(block);
        }

        private static boolean isBarrelUnmodifiable(Block block) {
            return !DoormatSettings.redstoneOpensBarrels && block instanceof BarrelBlock;
        }

        private static boolean hasDelay(Block block) {
            var value = Type.DELAY.getValue(block);

            if (value == null)
                return false;

            return (int) value > 0;
        }

        /**
         * @return true if the map for this rule entries has been modified.
         */
        public boolean hasBeenModified() {
            for (Block block : this.getBlocks()) {
                if (!this.isDefaultValue(block))
                    return true;
            }

            return false;
        }

        /**
         * Converts a list of all modifiable blocks (by <code>entries</code>) into a list of their keys, sorts them alphabetically, and then re-interprets the blocks from the keys.
         * @return a sorted list of blocks ordered alphabetically by their keys.
         */
        public List<Block> getBlocks() {
            List<String> strings = new ArrayList<>(Registries.BLOCK.stream().filter(this::canModify).map(TinkerKit::getKey).toList());
            Collections.sort(strings);

            Stream<String> stream = Arrays.stream(strings.toArray(String[]::new));
            Stream<Block> mapped = stream.map(key -> Registries.BLOCK.get(Identifier.tryParse(key)));

            return mapped.toList();
        }

        /**
         * Converts a list of all modifiable blocks (by <code>entries</code>) into a list of their keys and sorts them alphabetically.
         * @return a sorted array of all modifiable blocks' keys (by <code>entries</code>), used for command autocompletion.
         */
        public String[] getBlockKeys() {
            List<String> strings = new ArrayList<>(Registries.BLOCK.stream().filter(this::canModify).map(TinkerKit::getKey).toList());
            Collections.sort(strings);

            return strings.toArray(String[]::new);
        }

        private @Nullable Map<Block, Object> getValues(Block block) {
            Map<Block, Object> defaults = this.getDefaultMap();

            if (!DEFAULT_VALUES.containsKey(this) || !defaults.containsKey(block))
                return null;

            Map<Block, Object> map = new HashMap<>(defaults);
            Map<Block, Object> transients = this.getTransientMap();

            if (!TRANSIENT_VALUES.containsKey(this) || !transients.containsKey(block))
                return map;

            map.putAll(transients);

            return map;
        }

        /**
         * @param block the block to get the modified value of.
         * @return the default value assigned to the <code>block</code>.
         * @apiNote This requires you cast the return value depending on the {@link Type}.
         */
        public @Nullable Object getValue(Block block) {
            Map<Block, Object> map = this.getValues(block);

            if (map == null)
                return null;

            return map.get(block);
        }

        /**
         * @param block the block to get the default value of.
         * @return the default value assigned to the <code>block</code>.
         * @throws NullPointerException if no value can be found for the <code>block</code>.
         * @apiNote This requires you cast the return value depending on the {@link Type}.
         */
        public @Nullable Object getDefaultValue(Block block) {
            return DEFAULT_VALUES.containsKey(this) ? this.getDefaultMap().get(block) : null;
        }

        /**
         * @param block the block to get the modified value of.
         * @return the default value assigned to the <code>block</code>.
         * @apiNote This requires you cast the return value depending on the {@link Type}.
         */
        @Nullable Object getTransientValue(Block block) {
            return TRANSIENT_VALUES.containsKey(this) ? this.getTransientMap().get(block) : null;
        }

        /**
         * @return true if the map value assigned to this component is the default.
         */
        public boolean isDefaultValue(Block block) {
            return Objects.equals(this.getValue(block), this.getDefaultValue(block));
        }

        public void set(Block block, Object value) {
            if (!this.canModify(block))
                throw new IllegalArgumentException("Failed to set %s to a new %s value: %s!".formatted(getTranslatedName(block), this.name, value));

            if (!TRANSIENT_VALUES.containsKey(this)) {
                TRANSIENT_VALUES.put(this, new HashMap<>(Map.of(block, value)));
                return;
            }

            putSafely(this.getTransientMap(), block, value);
        }

        public void reset(Block block) {
            if (!TRANSIENT_VALUES.containsKey(this))
                return;

            Map<Block, Object> transients = this.getTransientMap();

            if (!transients.containsKey(block))
                return;

            transients.remove(block);

            if (transients.isEmpty())
                TRANSIENT_VALUES.remove(this);
        }

    }

    public static class Registry {

        /**
         * <strong>Should always be called from {@link ModInitializer#onInitialize()}.</strong>
         * @param block The block to assign the values to.
         * @param qc The quasi-connectivity value the <code>block</code> starts out with by default (usually 0).
         * @param delay The delay value the <code>block</code> starts out with by default (usually 0).
         * @param updateType The update entries value the <code>block</code> starts out with by default.
         * @param tickPriority The tick priority value the <code>block</code> starts out with by default.
         */
        public static void putBlock(@NotNull Block block, @Nullable Integer qc, @Nullable Integer delay, @Nullable UpdateType updateType, @Nullable TickPriority tickPriority) {
            for (Type type : Type.values()) {
                Object value = switch (type) {
                    case QC -> qc;
                    case DELAY -> delay;
                    case UPDATE_TYPE -> updateType;
                    case TICK_PRIORITY -> tickPriority;
                };

                if (value == null)
                    continue;

                putSafelyWithType(DEFAULT_VALUES, type, block, value);
            }
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

    }

}