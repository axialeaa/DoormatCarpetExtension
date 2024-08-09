package com.axialeaa.doormat.command;

import com.mojang.brigadier.arguments.ArgumentType;
import com.mojang.brigadier.arguments.IntegerArgumentType;

import java.util.stream.Stream;

import static com.axialeaa.doormat.tinker_kit.TinkerKit.Type;

public class DelayCommand extends AbstractTinkerKitCommand<Integer> {

    @Override
    public Type getType() {
        return Type.DELAY;
    }

    @Override
    public ArgumentType<Integer> getArgumentType() {
        return IntegerArgumentType.integer(0);
    }

    @Override
    public Stream<Integer> getSuggestions() {
        return Stream.of(0, 2, 4);
    }

}
