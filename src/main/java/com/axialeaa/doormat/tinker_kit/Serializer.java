package com.axialeaa.doormat.tinker_kit;

import com.google.gson.JsonElement;
import com.google.gson.JsonPrimitive;
import org.jetbrains.annotations.Nullable;

import java.util.function.Function;

/**
 *
 * @param deserializeFunc Defines a pathway from the writable value (stored in the json file) and a readable value (stored in the hashmaps). This allows us to extract the values from the json file, "jiggle them around" a bit, and save them to the transient map in a way that can be parsed by the game without any expensive and repetitive conversions.
 * @param serializeFunc Defines a pathway from the readable value (stored in the hashmaps) and a writable value (stored in the json file). This allows us to save the values from the transient map into a format json can parse.
 * @param fromJsonFunc Defines a pathway from a {@link JsonElement} to a writable value that can be operated on.
 * @param toJsonFunc Defines a pathway from a writable value to a {@link JsonPrimitive} that can be parsed by the config writer.
 * @param <W>
 * @param <R>
 */
public record Serializer<W, R>(Function<W, R> deserializeFunc, Function<R, W> serializeFunc, Function<JsonElement, W> fromJsonFunc, Function<W, JsonPrimitive> toJsonFunc) {

    public R deserialize(W value) {
        return this.deserializeFunc.apply(value);
    }

    public W serialize(R value) {
        return this.serializeFunc.apply(value);
    }

    public @Nullable R getValueFromElement(JsonElement element) {
        W value = this.fromJsonFunc.apply(element);
        return this.deserializeFunc.apply(value);
    }

    public JsonPrimitive getPrimitiveFromValue(R value) {
        W applied = serializeFunc.apply(value);
        return this.toJsonFunc.apply(applied);
    }

}
