package com.axialeaa.doormat.registry;

import com.axialeaa.doormat.Doormat;
import com.axialeaa.doormat.command.TinkerTypeCommand;
import com.axialeaa.doormat.setting.DoormatSettings;
import com.axialeaa.doormat.tinker_kit.UpdateType;
import com.mojang.brigadier.arguments.IntegerArgumentType;
import com.mojang.brigadier.arguments.StringArgumentType;
import net.minecraft.world.tick.TickPriority;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Stream;

public class DoormatTinkerTypeCommands {

    public static final List<TinkerTypeCommand<?, ?>> LIST = new ArrayList<>();

    public static final TinkerTypeCommand<Integer, Integer> QC = new TinkerTypeCommand<>(
        DoormatTinkerTypes.QC,
        DoormatSettings.commandQC,
        Integer.class,
        Stream.of(0, 1),
        IntegerArgumentType.integer(0, Doormat.MAX_QC_RANGE)
    );

    public static final TinkerTypeCommand<Integer, Integer> DELAY = new TinkerTypeCommand<>(
        DoormatTinkerTypes.DELAY,
        DoormatSettings.commandDelay,
        Integer.class,
        Stream.of(0, 2, 4),
        IntegerArgumentType.integer(0, 72000)
    );

    public static final TinkerTypeCommand<String, UpdateType> UPDATE_TYPE = new TinkerTypeCommand<>(
        DoormatTinkerTypes.UPDATE_TYPE,
        DoormatSettings.commandUpdateType,
        String.class,
        Stream.of(UpdateType.values()).map(updateType -> updateType.name().toLowerCase()),
        StringArgumentType.string()
    );

    public static final TinkerTypeCommand<Integer, TickPriority> TICK_PRIORITY = new TinkerTypeCommand<>(
        DoormatTinkerTypes.TICK_PRIORITY,
        DoormatSettings.commandTickPriority,
        Integer.class,
        Stream.of(TickPriority.values()).map(TickPriority::getIndex),
        IntegerArgumentType.integer(TickPriority.EXTREMELY_HIGH.getIndex(), TickPriority.EXTREMELY_LOW.getIndex())
    );

    public static void init() {
        Doormat.LOGGER.info("Registering {} Tinker Type commands!", Doormat.MOD_NAME);
    }

}
