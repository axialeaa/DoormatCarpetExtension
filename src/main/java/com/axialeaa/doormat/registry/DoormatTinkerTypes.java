package com.axialeaa.doormat.registry;

import com.axialeaa.doormat.Doormat;
import com.axialeaa.doormat.tinker_kit.TinkerType;
import com.axialeaa.doormat.tinker_kit.UpdateType;
import com.google.gson.JsonElement;
import com.google.gson.JsonPrimitive;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.tick.TickPriority;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class DoormatTinkerTypes {

    public static final List<TinkerType<?, ?>> LIST = new ArrayList<>();

    public static final TinkerType<Integer, Integer> QC = new TinkerType<>(
        "quasiconnectivity",
        integer -> integer,
        integer -> MathHelper.clamp(integer, 0, Doormat.MAX_QC_RANGE),
        JsonElement::getAsInt,
        JsonPrimitive::new
    );

    public static final TinkerType<Integer, Integer> DELAY = new TinkerType<>(
        "delay",
        integer -> integer,
        integer -> MathHelper.clamp(integer, 0, 72000),
        JsonElement::getAsInt,
        JsonPrimitive::new
    );

    public static final TinkerType<String, UpdateType> UPDATE_TYPE = new TinkerType<>(
        "updatetype",
        updateType -> updateType.name().toLowerCase(Locale.ROOT),
        string -> UpdateType.valueOf(string.toUpperCase(Locale.ROOT)),
        JsonElement::getAsString,
        JsonPrimitive::new
    );

    public static final TinkerType<Integer, TickPriority> TICK_PRIORITY = new TinkerType<>(
        "tickpriority",
        TickPriority::getIndex,
        TickPriority::byIndex,
        JsonElement::getAsInt,
        JsonPrimitive::new
    );

    public static void init() {
        Doormat.LOGGER.info("Registering {} Tinker Types!", Doormat.MOD_NAME);
    }

}
