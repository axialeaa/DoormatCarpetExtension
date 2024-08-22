package com.axialeaa.doormat.command.tinker_kit;

import com.axialeaa.doormat.util.UpdateType;
import com.mojang.brigadier.arguments.ArgumentType;
import com.mojang.brigadier.arguments.StringArgumentType;

import java.util.stream.Stream;

import static com.axialeaa.doormat.tinker_kit.TinkerKit.Type;

public class UpdateTypeCommand extends AbstractTinkerKitCommand<String> {

    @Override
    public Type getType() {
        return Type.UPDATE_TYPE;
    }

    @Override
    public Object getInputValue(String argument) {
        return UpdateType.valueOf(argument.toUpperCase());
    }

    @Override
    public ArgumentType<String> getArgumentType() {
        return StringArgumentType.string();
    }

    @Override
    public Class<String> getObjectClass() {
        return String.class;
    }

    @Override
    public Stream<String> getSuggestions() {
        return Stream.of(UpdateType.values()).map(updateType -> updateType.name().toLowerCase());
    }

}
