package com.axialeaa.doormat.command.tinker_kit;

import com.mojang.brigadier.arguments.ArgumentType;
import com.mojang.brigadier.arguments.IntegerArgumentType;
import net.minecraft.world.tick.TickPriority;

import java.util.stream.Stream;

import static com.axialeaa.doormat.tinker_kit.TinkerKit.Type;

public class TickPriorityCommand extends AbstractTinkerKitCommand<Integer> {

    @Override
    public Type getType() {
        return Type.TICK_PRIORITY;
    }

    @Override
    public ArgumentType<Integer> getArgumentType() {
        return IntegerArgumentType.integer(TickPriority.EXTREMELY_HIGH.getIndex(), TickPriority.EXTREMELY_LOW.getIndex());
    }

    @Override
    public Class<Integer> getObjectClass() {
        return Integer.class;
    }

    @Override
    public Stream<Integer> getSuggestions() {
        return Stream.of(TickPriority.values()).map(TickPriority::getIndex);
    }

}
