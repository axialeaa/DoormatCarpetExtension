package com.axialeaa.doormat.helper;

import net.minecraft.block.Block;
import net.minecraft.registry.Registries;
import net.minecraft.util.Identifier;

import java.util.Optional;

public class BlockIdentifierRuleHelper {

    /**
     * @return the block with the given namespace ID, or {@link Optional#empty()} if no such block exists.
     */
    public static Optional<Block> getBlockFromId(String id) {
        Identifier parsedId = Identifier.tryParse(id);
        return parsedId == null ? Optional.empty() : Optional.of(Registries.BLOCK.get(parsedId));
    }

}
