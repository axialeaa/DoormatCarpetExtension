package com.axialeaa.doormat.command;

import com.axialeaa.doormat.util.UpdateType;
import com.mojang.brigadier.arguments.ArgumentType;
import com.mojang.brigadier.arguments.StringArgumentType;
import com.mojang.brigadier.context.CommandContext;
import net.minecraft.server.command.ServerCommandSource;

import java.util.stream.Stream;

import static com.axialeaa.doormat.tinker_kit.TinkerKit.Type;

public class UpdateTypeCommand extends AbstractTinkerKitCommand<String> {

    @Override
    public Type getType() {
        return Type.UPDATE_TYPE;
    }

    @Override
    public Object getInputValue(CommandContext<ServerCommandSource> ctx, String name, String value) {
        UpdateType updateType;

        try {
            updateType = UpdateType.valueOf(value.toUpperCase());
        }
        catch (IllegalArgumentException e) {
            return null;
        }

        return updateType;
    }

    @Override
    public ArgumentType<String> getArgumentType() {
        return StringArgumentType.string();
    }

    @Override
    public Stream<String> getSuggestions() {
        return Stream.of(UpdateType.values()).map(UpdateType::toString);
    }

}
