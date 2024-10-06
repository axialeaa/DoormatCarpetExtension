package com.axialeaa.doormat;

import com.axialeaa.doormat.mixin.tinker_kit.ButtonBlockAccessor;
import com.axialeaa.doormat.registry.DoormatTinkerTypes;
import com.axialeaa.doormat.setting.DoormatSettings;
import com.axialeaa.doormat.tinker_kit.Entry;
import com.axialeaa.doormat.tinker_kit.TinkerKitInitializer;
import com.axialeaa.doormat.tinker_kit.TinkerKitRegistry;
import com.axialeaa.doormat.tinker_kit.UpdateType;
import net.minecraft.block.*;
import net.minecraft.world.tick.TickPriority;
import static com.axialeaa.doormat.registry.DoormatTinkerTypes.*;

public class DoormatTinkerKit implements TinkerKitInitializer {

    @Override
    public void registerTypes() {
        DoormatTinkerTypes.noop();
    }

    @Override
    public void registerBlocks() {
        TinkerKitRegistry.putBlocksByPredicate(
            block -> block instanceof DoorBlock ||
                block instanceof TrapdoorBlock ||
                block instanceof FenceGateBlock ||
                block instanceof AbstractSkullBlock ||
                block instanceof HopperBlock ||
                block instanceof BarrelBlock,
            new Entry<>(QC, 0),
            new Entry<>(DELAY, 0),
            new Entry<>(UPDATE_TYPE, UpdateType.SHAPE),
            new Entry<>(TICK_PRIORITY, TickPriority.NORMAL)
        );
        TinkerKitRegistry.putBlocksByPredicate(
            AbstractRedstoneGateBlock.class::isInstance,
            new Entry<>(QC, 0),
            new Entry<>(DELAY, 2),
            new Entry<>(UPDATE_TYPE, UpdateType.SHAPE)
        );
        TinkerKitRegistry.putBlocksByPredicate(
            DispenserBlock.class::isInstance,
            new Entry<>(QC, 1),
            new Entry<>(DELAY, 4),
            new Entry<>(UPDATE_TYPE, UpdateType.SHAPE),
            new Entry<>(TICK_PRIORITY, TickPriority.NORMAL)
        );
        TinkerKitRegistry.putBlocksByPredicate(
            block -> block instanceof RedstoneLampBlock ||
                block instanceof CrafterBlock,
            new Entry<>(QC, 0),
            new Entry<>(DELAY, 4),
            new Entry<>(UPDATE_TYPE, UpdateType.SHAPE),
            new Entry<>(TICK_PRIORITY, TickPriority.NORMAL)
        );
        TinkerKitRegistry.putBlocksByPredicate(
            BulbBlock.class::isInstance,
            new Entry<>(QC, 0),
            new Entry<>(DELAY, 4),
            new Entry<>(UPDATE_TYPE, UpdateType.BOTH),
            new Entry<>(TICK_PRIORITY, TickPriority.NORMAL)
        );
        TinkerKitRegistry.putBlocksByPredicate(
            PistonBlock.class::isInstance,
            new Entry<>(QC, 1),
            new Entry<>(DELAY, 0),
            new Entry<>(TICK_PRIORITY, TickPriority.NORMAL)
        );
        TinkerKitRegistry.putBlocksByPredicate(
            block -> block instanceof TntBlock ||
                block instanceof NoteBlock ||
                block instanceof BellBlock ||
                block instanceof PoweredRailBlock,
            new Entry<>(QC, 0),
            new Entry<>(DELAY, 0),
            new Entry<>(UPDATE_TYPE, UpdateType.BOTH),
            new Entry<>(TICK_PRIORITY, TickPriority.NORMAL)
        );
        TinkerKitRegistry.putBlocksByPredicate(
            block -> block instanceof CommandBlock ||
                block instanceof StructureBlock,
            new Entry<>(QC, 0),
            new Entry<>(DELAY, 1),
            new Entry<>(TICK_PRIORITY, TickPriority.NORMAL)
        );
        TinkerKitRegistry.putBlocksByPredicate(
            block -> block instanceof PressurePlateBlock ||
                block instanceof TargetBlock ||
                block instanceof DetectorRailBlock ||
                (block instanceof ButtonBlock buttonBlock &&
                    ((ButtonBlockAccessor) buttonBlock).getPressTicks() == 20),
            new Entry<>(DELAY, 20),
            new Entry<>(UPDATE_TYPE, UpdateType.BOTH),
            new Entry<>(TICK_PRIORITY, TickPriority.NORMAL)
        );
        TinkerKitRegistry.putBlocksByPredicate(
            block -> block instanceof WeightedPressurePlateBlock ||
                block instanceof TripwireHookBlock ||
                block == Blocks.CALIBRATED_SCULK_SENSOR,
            new Entry<>(DELAY, 10),
            new Entry<>(UPDATE_TYPE, UpdateType.BOTH),
            new Entry<>(TICK_PRIORITY, TickPriority.NORMAL)
        );
        TinkerKitRegistry.putBlocksByPredicate(
            DaylightDetectorBlock.class::isInstance,
            new Entry<>(DELAY, 20),
            new Entry<>(UPDATE_TYPE, UpdateType.BOTH)
        );
        TinkerKitRegistry.putBlocksByPredicate(
            LightningRodBlock.class::isInstance,
            new Entry<>(DELAY, 8),
            new Entry<>(UPDATE_TYPE, UpdateType.BOTH),
            new Entry<>(TICK_PRIORITY, TickPriority.NORMAL)
        );
        TinkerKitRegistry.putBlocksByPredicate(
            block -> block instanceof LecternBlock ||
                block instanceof RedstoneTorchBlock,
            new Entry<>(DELAY, 2),
            new Entry<>(UPDATE_TYPE, UpdateType.BOTH),
            new Entry<>(TICK_PRIORITY, TickPriority.NORMAL)
        );
        TinkerKitRegistry.putBlocksByPredicate(
            TrappedChestBlock.class::isInstance,
            new Entry<>(UPDATE_TYPE, UpdateType.BOTH)
        );
        TinkerKitRegistry.putBlocksByPredicate(
            RailBlock.class::isInstance,
            new Entry<>(DELAY, 0),
            new Entry<>(UPDATE_TYPE, UpdateType.BOTH),
            new Entry<>(TICK_PRIORITY, TickPriority.NORMAL)
        );
        TinkerKitRegistry.putBlocksByPredicate(
            ObserverBlock.class::isInstance,
            new Entry<>(DELAY, 2),
            new Entry<>(UPDATE_TYPE, UpdateType.SHAPE),
            new Entry<>(TICK_PRIORITY, TickPriority.NORMAL)
        );
        TinkerKitRegistry.putBlocksByPredicate(
            WallRedstoneTorchBlock.class::isInstance,
            new Entry<>(QC, 0),
            new Entry<>(DELAY, 2),
            new Entry<>(UPDATE_TYPE, UpdateType.BOTH),
            new Entry<>(TICK_PRIORITY, TickPriority.NORMAL)
        );
        TinkerKitRegistry.putBlocksByPredicate(
            BigDripleafBlock.class::isInstance,
            new Entry<>(QC, 0),
            new Entry<>(UPDATE_TYPE, UpdateType.SHAPE)
        );
        TinkerKitRegistry.putBlock(Blocks.SCULK_SENSOR,
            new Entry<>(DELAY, 30),
            new Entry<>(UPDATE_TYPE, UpdateType.BOTH),
            new Entry<>(TICK_PRIORITY, TickPriority.NORMAL)
        );
        TinkerKitRegistry.putBlocksByPredicate(
            block -> block instanceof ButtonBlock buttonBlock &&
                ((ButtonBlockAccessor) buttonBlock).getPressTicks() == 30,
            new Entry<>(DELAY, 30),
            new Entry<>(UPDATE_TYPE, UpdateType.BOTH),
            new Entry<>(TICK_PRIORITY, TickPriority.NORMAL)
        );
    }

    @Override
    public void registerModificationPredicates() {
        TinkerKitRegistry.putModificationPredicate((block, tinkerType) -> {
            if (DoormatSettings.redstoneOpensBarrels)
                return true;

            return !(block instanceof BarrelBlock);
        });
        TinkerKitRegistry.putModificationPredicate((block, tinkerType) -> {
            if (tinkerType != DoormatTinkerTypes.TICK_PRIORITY)
                return true;

            var value = DoormatTinkerTypes.DELAY.getValue(block);

            return value != null && value > 0;
        });
    }

}
