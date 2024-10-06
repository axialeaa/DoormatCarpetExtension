package com.axialeaa.doormat.tinker_kit;

import carpet.CarpetSettings;
import carpet.utils.Translations;
import com.axialeaa.doormat.Doormat;
import com.axialeaa.doormat.registry.DoormatTinkerTypes;
import com.axialeaa.doormat.setting.DoormatSettings;
import net.minecraft.block.AbstractRedstoneGateBlock;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.fluid.FluidState;
import net.minecraft.registry.Registries;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.world.RedstoneView;
import net.minecraft.world.World;
import net.minecraft.world.tick.TickPriority;
import org.spongepowered.asm.mixin.injection.ModifyArg;
import java.util.ArrayList;
import java.util.List;
import java.util.function.BiPredicate;
import java.util.function.Predicate;

public class TinkerKitUtils {

    /**
     * <p>A modification predicate is a boolean condition that must be satisfied in order for a certain block's behaviour to be modified by a {@link TinkerType}. This allows for the modification's availability to change at any time during runtime unlike the predicates we may have used to {@link TinkerKitRegistry#putBlocksByPredicate(Predicate, Entry[]) register a block}.
     *
     * <p>This leaves us with a lot of power to change the way the <b>Tinker Kit</b> operates, and we should use it with caution. It is unwise to create a predicate that completely suppresses the ability to modify a block's behaviour, for example.
     */
    static final List<BiPredicate<Block, TinkerType<?, ?>>> MODIFICATION_PREDICATES = new ArrayList<>();

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
        String path = Doormat.TRANSLATION_PATH.formatted(namespace, CarpetSettings.language);

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
    public static boolean isReceivingRedstonePower(RedstoneView world, BlockPos pos, Block block, int i) {
        if (!DoormatTinkerTypes.QC.canModify(block))
            return world.isReceivingRedstonePower(pos);

        var value = DoormatTinkerTypes.QC.getValue(block);

        if (value == null)
            return world.isReceivingRedstonePower(pos);

        for (int j = 0; j <= value + i; j++) {
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
        return isEmittingRedstonePower(world, pos, block, direction, 0);
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

        if (!DoormatTinkerTypes.QC.canModify(block))
            return power;

        var value = DoormatTinkerTypes.QC.getValue(block);

        if (value == null)
            return power;

        for (int j = 0; j <= value + i; j++) {
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
        if (!DoormatTinkerTypes.DELAY.canModify(block))
            return fallback;

        var value = DoormatTinkerTypes.DELAY.getValue(block);

        if (value == null)
            return fallback;

        return value;
    }

    /**
     * @param block the block this method checks for.
     * @param fallback the default flags this method should fall back to if it can't find the default value in the registry map (should be the third parameter in the call to {@link World#setBlockState(BlockPos, BlockState, int)}, obtained by default when using {@link ModifyArg} to index 2).
     * @return the update entries flag(s) for the given blockstate.
     */
    public static int getFlags(Block block, int fallback) {
        if (!DoormatTinkerTypes.UPDATE_TYPE.canModify(block))
            return fallback;

        var value = DoormatTinkerTypes.UPDATE_TYPE.getValue(block);

        if (value == null)
            return fallback;

        return value.flags;
    }

    /**
     * Alternative implementation of {@link TinkerKitUtils#getFlags(Block, int)} that has the ability to set certain bits to 0, effectively bypassing certain types of update regardless of the input.
     */
    public static int getFlagsWithRemoved(Block block, int fallback, int removed) {
        return getFlags(block, fallback) & ~removed;
    }

    /**
     * Alternative implementation of {@link TinkerKitUtils#getFlagsWithRemoved(Block, int, int)} that takes in an {@link UpdateType} object instead of an integer.
     */
    public static int getFlagsWithRemoved(Block block, int fallback, UpdateType updateType) {
        return getFlagsWithRemoved(block, fallback, updateType.flags);
    }

    /**
     * Alternative implementation of {@link TinkerKitUtils#getFlagsWithRemoved(Block, int, int)} that removes neighbor updates.
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
     * An alternative implementation of {@link TinkerKitUtils#shouldUpdateNeighbours(Block, int)} which assumes a default fallback value equivalent to {@link Block#NOTIFY_ALL}.
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
        if (!DoormatTinkerTypes.TICK_PRIORITY.canModify(block))
            return fallback;

        var value = DoormatTinkerTypes.TICK_PRIORITY.getValue(block);

        if (value == null)
            return fallback;

        return value;
    }

    public static TickPriority getTickPriority(Block block) {
        return getTickPriority(block, TickPriority.NORMAL);
    }

}