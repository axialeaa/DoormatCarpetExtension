package com.axialeaa.doormat.command.tinker_kit;

import com.mojang.brigadier.arguments.ArgumentType;
import com.mojang.brigadier.arguments.IntegerArgumentType;
import static com.axialeaa.doormat.tinker_kit.TinkerKit.Type;

public class DelayCommand extends AbstractTinkerKitCommand<Integer> {

    @Override
    public Type getType() {
        return Type.DELAY;
    }

    @Override
    public ArgumentType<Integer> getArgumentType() {
        return IntegerArgumentType.integer(0, 72000);
    }

    @Override
    public Class<Integer> getObjectClass() {
        return Integer.class;
    }

}
