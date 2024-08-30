package com.axialeaa.doormat.setting.validator;

import carpet.api.settings.CarpetRule;
import carpet.api.settings.Validator;
import net.minecraft.block.Block;
import net.minecraft.registry.Registries;
import net.minecraft.server.command.ServerCommandSource;
import net.minecraft.util.Identifier;
import org.jetbrains.annotations.Nullable;
import java.util.ArrayList;
import java.util.List;

public class BlockIdentifierValidator extends Validator<String> {

    private static final List<String> BLOCK_IDS = Registries.BLOCK.getIds().stream().map(String::valueOf).toList();

    @Override
    public String validate(@Nullable ServerCommandSource source, CarpetRule<String> changingRule, String newValue, String userInput) {
        for (String id : newValue.split(",")) {
            if (!BLOCK_IDS.contains(id))
                return null;
        }

        return newValue;
    }

    @Override
    public String description() {
        return "You must choose any number of valid block IDs separated by commas";
    }

    public static Block getRandomBlockFromString(String string, Block fallback) {
        List<Block> blocks = getBlocksFromString(string);

        if (blocks == null || blocks.isEmpty())
            return fallback;

        return blocks.get((int) (Math.random() * blocks.size()));
    }

    public static List<Block> getBlocksFromString(String string) {
        return getBlocksFromStrings(string.split(","));
    }

    public static List<Block> getBlocksFromStrings(String[] strings) {
        List<Block> blocks = new ArrayList<>();

        for (String id : strings) {
            Identifier parsedId = Identifier.tryParse(id);

            if (parsedId == null)
                continue;

            blocks.add(Registries.BLOCK.get(parsedId));
        }

        return blocks.isEmpty() ? null : blocks;
    }

}