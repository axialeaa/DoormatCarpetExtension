package com.axialeaa.doormat.registry;

import com.axialeaa.doormat.setting.DoormatSettings;
import com.axialeaa.doormat.tinker_kit.Serializer;
import com.axialeaa.doormat.tinker_kit.TinkerType;
import com.axialeaa.doormat.tinker_kit.UpdateType;
import com.google.gson.JsonElement;
import com.google.gson.JsonPrimitive;
import com.mojang.brigadier.arguments.IntegerArgumentType;
import com.mojang.brigadier.arguments.StringArgumentType;
import net.minecraft.world.tick.TickPriority;
import java.util.function.Function;
import java.util.stream.Stream;

public class DoormatTinkerTypes {

    public static final TinkerType<Integer, Integer> QC = new TinkerType<>(
        "quasiconnectivity",
        Integer.class,

        DoormatSettings.commandQC,
        Stream.of(0, 1),
        IntegerArgumentType.integer(0),

        new Serializer<>(
            Function.identity(),
            Function.identity(),
            JsonElement::getAsInt,
            JsonPrimitive::new
        )
    );

    public static final TinkerType<Integer, Integer> DELAY = new TinkerType<>(
        "delay",
        Integer.class,

        DoormatSettings.commandDelay,
        Stream.of(0, 2, 4),
        IntegerArgumentType.integer(0, 72000),

        new Serializer<>(
            Function.identity(),
            Function.identity(),
            JsonElement::getAsInt,
            JsonPrimitive::new
        )
    );

    public static final TinkerType<String, UpdateType> UPDATE_TYPE = new TinkerType<>(
        "updatetype",
        String.class,

        DoormatSettings.commandUpdateType,
        Stream.of(UpdateType.values()),
        StringArgumentType.string(),

        new Serializer<>(
            UpdateType::valueOf,
            Enum::name,
            JsonElement::getAsString,
            JsonPrimitive::new
        )
    );

    public static final TinkerType<Integer, TickPriority> TICK_PRIORITY = new TinkerType<>(
        "tickpriority",
        Integer.class,

        DoormatSettings.commandTickPriority,
        Stream.of(TickPriority.values()),
        IntegerArgumentType.integer(TickPriority.EXTREMELY_HIGH.getIndex(), TickPriority.EXTREMELY_LOW.getIndex()),

        new Serializer<>(
            TickPriority::byIndex,
            TickPriority::getIndex,
            JsonElement::getAsInt,
            JsonPrimitive::new
        )
    );

    public static void noop() {}

}
