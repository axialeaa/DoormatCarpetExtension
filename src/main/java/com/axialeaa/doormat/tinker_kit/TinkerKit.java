package com.axialeaa.doormat.tinker_kit;

import carpet.CarpetSettings;
import carpet.utils.Translations;
import com.axialeaa.doormat.DoormatServer;
import com.axialeaa.doormat.DoormatSettings;
import com.axialeaa.doormat.util.UpdateType;
import net.minecraft.block.*;
import net.minecraft.fluid.FluidState;
import net.minecraft.registry.Registries;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.world.RedstoneView;
import net.minecraft.world.World;
import org.jetbrains.annotations.ApiStatus;
import org.spongepowered.asm.mixin.injection.ModifyArg;

import java.util.*;
import java.util.stream.Stream;

@ApiStatus.NonExtendable
@ApiStatus.Experimental
public class TinkerKit {

    /**
     * This hashmap is a little different to {@link TinkerKitRegistry#DEFAULT_QC_VALUES} as it stores the blocks alongside their dynamic, modified values. While the registry map is put-to in {@link DoormatServer#onInitialize()} and changes only when the game starts up, this can change at any time during gameplay.
     * @implNote This falls back to the values specified in the registry map before amending itself later on in runtime, thanks to {@link ConfigFile#loadFromFile(MinecraftServer)}. This just adds a level of robustness in case the game crashes!
     */
    public static final Map<Block, Integer> MODIFIED_QC_VALUES = new HashMap<>();
    /**
     * This hashmap is a little different to {@link TinkerKitRegistry#DEFAULT_UPDATE_TYPE_VALUES} as it stores the blocks alongside their dynamic, modified values. While the registry map is put-to in {@link DoormatServer#onInitialize()} and changes only when the game starts up, this can change at any time during gameplay.
     * @implNote This falls back to the values specified in the registry map before amending itself later on in runtime, thanks to {@link ConfigFile#loadFromFile(MinecraftServer)}. This just adds a level of robustness in case the game crashes!
     */
    public static final Map<Block, UpdateType> MODIFIED_UPDATE_TYPE_VALUES = new HashMap<>();

    static {
        try {
            MODIFIED_QC_VALUES.putAll(TinkerKitRegistry.getDefaultQCValues());
            MODIFIED_UPDATE_TYPE_VALUES.putAll(TinkerKitRegistry.getDefaultUpdateTypeValues());

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

        Type.QC.warn(block);

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
     * This has been re-implemented due to diodes ({@link AbstractRedstoneGateBlock}<code>s</code>) taking in integer inputs instead of booleans like most other components; they getFromFlags told <i>if</i> they're powered, not to "what degree".
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

        Type.QC.warn(block);

        return world.getEmittedRedstonePower(pos, direction);
    }

    /**
     * An alternative implementation of {@link #isReceivingRedstonePower(RedstoneView, BlockPos, BlockState)} which instead outputs a signal strength--used for diodes.
     * <p>
     * This has been re-implemented due to diodes ({@link AbstractRedstoneGateBlock}<code>s</code>) taking in integer inputs instead of booleans like most other components; they getFromFlags told <i>if</i> they're powered, not to "what degree".
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
     * @param fallback the default flags this method should fall back to if it can't find the default value in the registry map (should be the third parameter in the call to {@link World#setBlockState(BlockPos, BlockState, int)}, obtained by default when using {@link ModifyArg} to index 2).
     * @return the update type flag(s) for the given blockstate.
     */
    public static int getFlags(BlockState state, int fallback) {
        Block block = state.getBlock();
        
        if (!Type.UPDATE_TYPE.canModify(block)) {
            Type.UPDATE_TYPE.warn(block);
            return fallback;
        }

        return ((UpdateType) Type.UPDATE_TYPE.getModifiedValue(block)).getFlags();
    }

    /**
     * An alternative implementation of {@link World#removeBlock(BlockPos, boolean)}, allowing for custom update flags based on the block at the position this method is called from.
     * @param world the world this method is called in.
     * @param pos the block position this method is called at.
     * @param move if the block is moving.
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
     * Used for multiple instances of needing to specify which type of redstone rule to modify via commands or the config file.
     */
    public enum Type {

        QC          ("quasi-connectivity", TinkerKitRegistry.getDefaultQCValues(), MODIFIED_QC_VALUES),
        UPDATE_TYPE ("update type", TinkerKitRegistry.getDefaultUpdateTypeValues(), MODIFIED_UPDATE_TYPE_VALUES);

        private final String name;
        private final Map<Block, ?> defaultValues;
        private final Map<Block, ?> modifiedValues;

        Type(String name, Map<Block, ?> defaultValues, Map<Block, ?> modifiedValues) {
            this.name = name;
            this.defaultValues = defaultValues;
            this.modifiedValues = modifiedValues;
        }

        public String getName() {
            return name;
        }

        public Map<Block, ?> getDefaultValues() {
            return this.defaultValues;
        }

        public Map<Block, ?> getModifiedValues() {
            return this.modifiedValues;
        }

        /**
         * @return true if this rule type has support for the component.
         */
        public boolean canModify(Block block) {
            if (!DoormatSettings.redstoneOpensBarrels && block instanceof BarrelBlock)
                return false;

            return this.getDefaultValues().containsKey(block);
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
         * @apiNote This requires you cast the return value to either {@link Integer} or {@link UpdateType} depending on the {@link Type}.
         */
        public Object getDefaultValue(Block block) {
            try {
                return this.getDefaultValues().get(block);
            }
            catch (NullPointerException e) {
                throw new NullPointerException("Failed to find the default " + this.getName() + " value for " + getTranslatedName(block) + "!");
            }
        }

        /**
         * @param block the block to get the modified value of.
         * @return the default value assigned to the <code>block</code>.
         * @throws NullPointerException if no value can be found for the <code>block</code>.
         * @apiNote This requires you cast the return value to either {@link Integer} or {@link UpdateType} depending on the {@link Type}.
         */
        public Object getModifiedValue(Block block) {
            try {
                return this.getModifiedValues().get(block);
            }
            catch (NullPointerException e) {
                throw new NullPointerException("Failed to find the modified " + this.getName() + " value for " + getTranslatedName(block) + "!");
            }
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
                    switch (this) {
                        case QC -> MODIFIED_QC_VALUES.put(block, (int) value);
                        case UPDATE_TYPE -> MODIFIED_UPDATE_TYPE_VALUES.put(block, (UpdateType) value);
                    }

                    return true;
                }
                catch (Exception ignored) {}
            }

            return false;
        }

        public void set(Block block, Object value) {
            if (!trySet(block, value))
                throw new IllegalArgumentException("Failed to set the " + this.getName() + " value of " + getTranslatedName(block) + " to " + value.toString() + "!");
        }

        public void reset(Block block) {
            if (!trySet(block, this.getDefaultValue(block)))
                throw new IllegalArgumentException("Failed to set " + getTranslatedName(block) + " to its default " + this.getName() + " value!");
        }

        public void warn(Block block) {
            String s = switch (this) {
                case QC -> "vanilla call";
                case UPDATE_TYPE -> "fallback argument";
            };
            DoormatServer.LOGGER.warn("{} does not support {} modification! Returning {}.", getTranslatedName(block), this.getName(), s);
        }

    }

}