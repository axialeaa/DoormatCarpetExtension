package com.axialeaa.doormat.settings.validators;

import carpet.api.settings.CarpetRule;
import carpet.api.settings.Validator;
import net.minecraft.block.Block;
import net.minecraft.registry.Registries;
import net.minecraft.server.command.ServerCommandSource;
import net.minecraft.util.Identifier;
import org.jetbrains.annotations.Nullable;
import java.util.List;
import java.util.Optional;

public class BlockIdentifierValidator extends Validator<String> {

    private final List<String> BLOCKS = Registries.BLOCK.getIds().stream().map(String::valueOf).toList();

    @Override
    public String validate(@Nullable ServerCommandSource source, CarpetRule<String> changingRule, String newValue, String userInput) {
        return BLOCKS.contains(newValue) ? newValue : null;
    }

    @Override
    public String description() {
        return "You must choose a valid block ID";
    }

    public static Optional<Block> getBlockFromId(String id) {
        Identifier parsedId = Identifier.tryParse(id);
        return parsedId == null ? Optional.empty() : Optional.of(Registries.BLOCK.get(parsedId));
    }

}