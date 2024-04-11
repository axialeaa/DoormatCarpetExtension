package com.axialeaa.doormat.tinker_kit;

import carpet.CarpetServer;
import carpet.CarpetSettings;
import carpet.utils.Translations;
import com.axialeaa.doormat.DoormatServer;
import com.axialeaa.doormat.DoormatSettings;
import com.axialeaa.doormat.util.UpdateType;
import net.minecraft.block.AbstractRedstoneGateBlock;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.fluid.FluidState;
import net.minecraft.registry.Registries;
import net.minecraft.resource.featuretoggle.FeatureFlags;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.Identifier;
import net.minecraft.util.StringIdentifiable;
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
     * Used for multiple instances of needing to specify which type of redstone rule to modify via commands or the config file.
     */
    public enum ModificationType implements StringIdentifiable {

        QC          ("quasi-connectivity"),
        UPDATE_TYPE ("update type");

        private final String name;

        ModificationType(String name) {
            this.name = name;
        }

        @Override
        public String asString() {
            return this.name;
        }

    }

    /**
     * This hashmap is a little different to {@link TinkerKitRegistry#DEFAULT_QC_VALUES} as it stores the blocks alongside their dynamic, modified values. While the registry map is put-to in {@link DoormatServer#onInitialize()} and changes only when the game starts up, this can and should change at any time during gameplay.
     * @implNote This falls back to the values specified in the registry map before amending itself later on in runtime, thanks to {@link ConfigFile#loadFromFile(MinecraftServer)}. This just adds a level of robustness in case the game crashes!
     */
    public static final Map<Block, Integer> MODIFIED_QC_VALUES = new HashMap<>();
    /**
     * This hashmap is a little different to {@link TinkerKitRegistry#DEFAULT_UPDATE_TYPE_VALUES} as it stores the blocks alongside their dynamic, modified values. While the registry map is put-to in {@link DoormatServer#onInitialize()} and changes only when the game starts up, this can and should change at any time during gameplay.
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
        return String.valueOf(Registries.BLOCK.getId(block));
    }

    /**
     * @param block the block to get the translated name of.
     * @return the name of the block translated via the lang file on the server side, thanks to {@link Translations}.
     */
    public static String getTranslatedName(Block block) {
        String key = block.getTranslationKey();
        String namespace = Registries.BLOCK.getId(block).getNamespace();
        String path = String.format("assets/" + namespace + "/lang/%s.json", CarpetSettings.language);

        return Translations.getTranslationFromResourcePath(path).get(key);
    }

    private static Map<Block, ?> getDefaultValues(ModificationType type) {
        return switch (type) {
            case QC -> TinkerKitRegistry.getDefaultQCValues();
            case UPDATE_TYPE -> TinkerKitRegistry.getDefaultUpdateTypeValues();
        };
    }

    private static Map<Block, ?> getModifiedValues(ModificationType type) {
        return switch (type) {
            case QC -> MODIFIED_QC_VALUES;
            case UPDATE_TYPE -> MODIFIED_UPDATE_TYPE_VALUES;
        };
    }

    /**
     * @param block the block to get the default value of.
     * @param type the type of rule to search for the value in.
     * @return the default value of the specified <code>type</code> assigned to the <code>block</code>.
     * @throws NullPointerException if no value can be found for the <code>block</code>.
     * @apiNote This requires you cast the return value to either {@link Integer} or {@link UpdateType} depending on the <code>type</code>.
     */
    public static Object getDefaultValue(Block block, ModificationType type) {
        try {
            return getDefaultValues(type).get(block);
        }
        catch (NullPointerException e) {
            throw new NullPointerException("Failed to find the " + type.asString() + " value for " + getTranslatedName(block) + "!");
        }
    }

    /**
     * Sets the map value assigned to this component to the default.
     */
    public static void setDefaultValue(Block block, ModificationType type) {
        if (isModifiable(block, type))
            switch (type) {
                case QC -> MODIFIED_QC_VALUES.put(block, (int) getDefaultValue(block, type));
                case UPDATE_TYPE -> MODIFIED_UPDATE_TYPE_VALUES.put(block, (UpdateType) getDefaultValue(block, type));
            }
        else throw new IllegalArgumentException("Failed to set " + getTranslatedName(block) + " to its default " + type.asString() + " value!");
    }

    /**
     * @return true if the map value assigned to this component is the default.
     */
    public static boolean isDefaultValue(Block block, ModificationType type) {
        return getModifiedValues(type).get(block) == getDefaultValue(block, type);
    }

    public static boolean isMapModified(ModificationType type) {
        for (Block block : getModifiableBlocks(type).toList()) {
            if (!isDefaultValue(block, type))
                return true;
        }

        return false;
    }

    /**
     * @return true if the specified rule type has support for this component.
     */
    public static boolean isModifiable(Block block, ModificationType type) {
        if (block.getRequiredFeatures().contains(FeatureFlags.UPDATE_1_21) && !DoormatServer.hasExperimentalDatapack(CarpetServer.minecraft_server))
            return false;

        if (!DoormatSettings.redstoneOpensBarrels && block == Blocks.BARREL)
            return false;

        return getDefaultValues(type).containsKey(block);
    }

    /**
     * Converts a list of all modifiable blocks (by <code>type</code>) into a list of their keys and sorts them alphabetically.
     * @return a sorted array of all modifiable blocks' keys (by <code>type</code>), used for command autocompletion.
     */
    public static String[] getModifiableKeys(ModificationType type) {
        List<String> strings = new ArrayList<>(Registries.BLOCK.stream().filter(block -> isModifiable(block, type)).map(TinkerKit::getKey).toList());
        Collections.sort(strings);

        return strings.toArray(String[]::new);
    }

    /**
     * Converts a list of all modifiable blocks (by <code>type</code>) into a list of their keys, sorts them alphabetically, and then re-interprets the blocks from the keys.
     * @return a sorted stream of blocks ordered alphabetically by their keys.
     */
    public static Stream<Block> getModifiableBlocks(ModificationType type) {
        List<String> strings = new ArrayList<>(Registries.BLOCK.stream().filter(block -> isModifiable(block, type)).map(TinkerKit::getKey).toList());
        Collections.sort(strings);

        return Arrays.stream(strings.toArray(String[]::new)).map(key -> Registries.BLOCK.get(Identifier.tryParse(key)));
    }

    /**
     * @return a stream of blocks filtered by whether they appear in the registry map.
     */
//    @ApiStatus.Internal
//    public static Stream<Block> getBlocks(ModificationType type) {
//        return Registries.BLOCK.stream().filter(getDefaultValues(type)::containsKey);
//    }

    public static boolean cannotQC(RedstoneView world, BlockPos pos) {
        return world.isOutOfHeightLimit(pos) || DoormatSettings.qcSuppressor && world.getBlockState(pos).isOf(Blocks.EMERALD_ORE);
    }

    /**
     * Sends a log warning to indicate that the <code>state</code> cannot be modified by the rule <code>type</code>.
     * @param state The blockstate to suggest is unmodifiable.
     * @param type The type of rule to send the warning through.
     */
    private static void sendUnmodifiableWarning(BlockState state, ModificationType type) {
        DoormatServer.LOGGER.warn("{} does not support {} modification!", getTranslatedName(state.getBlock()), type.asString());
    }

    /**
     * @param world the world this method is called in.
     * @param pos the block position this method is called at.
     * @param i the number to add onto the range check (useful for doors).
     * @return true if any of the block positions in the specified range are receiving power, otherwise false.
     */
    public static boolean isReceivingRedstonePower(RedstoneView world, BlockPos pos, int i) {
        BlockState blockState = world.getBlockState(pos);

        if (isModifiable(blockState.getBlock(), ModificationType.QC)) {
            for (int j = 0; j <= MODIFIED_QC_VALUES.get(blockState.getBlock()) + i; j++) {
                BlockPos blockPos = pos.up(j);

                if (cannotQC(world, blockPos))
                    break;

                if (world.isReceivingRedstonePower(blockPos))
                    return true;
            }
        }
        else sendUnmodifiableWarning(blockState, ModificationType.QC);

        return world.isReceivingRedstonePower(pos);
    }

    /**
     * @param world the world this method is called in.
     * @param pos the block position this method is called at.
     * @return true if any of the block positions in the specified range are receiving power, otherwise false.
     */
    public static boolean isReceivingRedstonePower(RedstoneView world, BlockPos pos) {
        return isReceivingRedstonePower(world, pos, 0);
    }

    /**
     * An alternative implementation of {@link #isReceivingRedstonePower(RedstoneView, BlockPos, int)} which takes in a direction--used for redstone torches.
     * <p>
     * This has been re-implemented due to the semantic difference between {@link net.minecraft.world.RedstoneView#isEmittingRedstonePower(BlockPos, Direction) isEmittingRedstonePower()} and {@link net.minecraft.world.RedstoneView#isReceivingRedstonePower(BlockPos) isReceivingRedstonePower()}. The second will return true if any of the blocks adjacent to the block position are powered, whereas the first checks for only the block position itself. This makes sense for torches and really nothing else, which unpower when the block they're resting on is sourcing power.
     * @param world the world this method is called in.
     * @param pos the block position this method is called at.
     * @param direction the direction to check if power is being received.
     * @param i the number to add onto the range check.
     * @return true if any of the block positions in the specified range are receiving power, otherwise false.
     */
    public static boolean isReceivingRedstonePower(RedstoneView world, BlockPos pos, Direction direction, int i) {
        return getEmittedRedstonePower(world, pos, direction, i) > 0;
    }

    /**
     * An alternative implementation of {@link #isReceivingRedstonePower(RedstoneView, BlockPos)} which takes in a direction--used for redstone torches.
     * <p>
     * This has been re-implemented due to the semantic difference between {@link net.minecraft.world.RedstoneView#isEmittingRedstonePower(BlockPos, Direction) isEmittingRedstonePower()} and {@link net.minecraft.world.RedstoneView#isReceivingRedstonePower(BlockPos) isReceivingRedstonePower()}. The second will return true if any of the blocks adjacent to the block position are powered, whereas the first checks for only the block position itself. This makes sense for torches and really nothing else, which unpower when the block they're resting on is sourcing power.
     * @param world the world this method is called in.
     * @param pos the block position this method is called at.
     * @param direction the direction to check if power is being received.
     * @return true if any of the block positions in the specified range are receiving power, otherwise false.
     */
    public static boolean isReceivingRedstonePower(RedstoneView world, BlockPos pos, Direction direction) {
        return getEmittedRedstonePower(world, pos, direction, 0) > 0;
    }

    /**
     * An alternative implementation of {@link #isReceivingRedstonePower(RedstoneView, BlockPos, int)} which instead outputs a signal strength--used for diodes.
     * <p>
     * This has been re-implemented due to diodes ({@link AbstractRedstoneGateBlock}<code>s</code>) taking in integer inputs instead of booleans like most other components; they get told <i>if</i> they're powered, not to "what degree".
     * @param world the world this method is called in.
     * @param pos the block position this method is called at.
     * @param direction the direction to check if power is being received.
     * @param i the number to add onto the range check.
     * @return the largest signal strength within the quasi-connectivity range.
     */
    public static int getEmittedRedstonePower(RedstoneView world, BlockPos pos, Direction direction, int i) {
        BlockState blockState = world.getBlockState(pos);
        int power = 0;

        if (isModifiable(blockState.getBlock(), ModificationType.QC)) {
            for (int j = 0; j <= MODIFIED_QC_VALUES.get(blockState.getBlock()) + i; j++) {
                BlockPos blockPos = pos.offset(direction).up(j);

                if (cannotQC(world, blockPos) || power >= 15)
                    break;

                power = Math.max(power, world.getEmittedRedstonePower(blockPos, direction));
            }
        }
        else sendUnmodifiableWarning(blockState, ModificationType.QC);

        return world.getEmittedRedstonePower(pos, direction);
    }

    /**
     * An alternative implementation of {@link #isReceivingRedstonePower(RedstoneView, BlockPos)} which instead outputs a signal strength--used for diodes.
     * <p>
     * This has been re-implemented due to diodes ({@link AbstractRedstoneGateBlock}<code>s</code>) taking in integer inputs instead of booleans like most other components; they get told <i>if</i> they're powered, not to "what degree".
     * @param world the world this method is called in.
     * @param pos the block position this method is called at.
     * @param direction the direction to check if power is being received.
     * @return the largest signal strength within the quasi-connectivity range.
     */
    public static int getEmittedRedstonePower(RedstoneView world, BlockPos pos, Direction direction) {
        return getEmittedRedstonePower(world, pos, direction, 0);
    }

    /**
     * @param state the blockstate this method checks for.
     * @param fallback the default flags this method should fall back to if it can't find the default value in the registry map (should be the third parameter in the call to {@link World#setBlockState(BlockPos, BlockState, int)}, obtained by default when using {@link ModifyArg} to index 2).
     * @return the update type flag(s) for the given blockstate.
     */
    public static int getUpdateFlags(BlockState state, int fallback) {
        if (!isModifiable(state.getBlock(), ModificationType.UPDATE_TYPE)) {
            sendUnmodifiableWarning(state, ModificationType.UPDATE_TYPE);
            return fallback;
        }
        return getDefaultValue(state.getBlock(), ModificationType.UPDATE_TYPE) == null ? fallback : MODIFIED_UPDATE_TYPE_VALUES.get(state.getBlock()).getFlags();
    }

    /**
     * An alternative implementation of {@link World#removeBlock(BlockPos, boolean)}, allowing for custom update flags based on the block at the position this method is called from.
     * @param world the world this method is called in.
     * @param pos the block position this method is called at.
     * @param move if the block is moving.
     */
    public static boolean removeBlock(World world, BlockPos pos, boolean move) {
        FluidState fluidState = world.getFluidState(pos);
        return world.setBlockState(pos, fluidState.getBlockState(), getUpdateFlags(fluidState.getBlockState(), Block.NOTIFY_ALL) | (move ? Block.MOVED : 0));
    }

    /**
     * @param state the blockstate this method checks for.
     * @param fallback the default flags this method should fall back to if it can't find the default value in the registry map (should be the third parameter in the call to {@link World#setBlockState(BlockPos, BlockState, int)}, obtained by default when using {@link ModifyArg} to index 2).
     * @return true if the update type flags of the given blockstate are odd (BLOCK, 1 and BOTH, 3).
     */
    public static boolean shouldUpdateNeighbours(BlockState state, int fallback) {
        return (getUpdateFlags(state, fallback) & Block.NOTIFY_NEIGHBORS) == 1;
    }

}

