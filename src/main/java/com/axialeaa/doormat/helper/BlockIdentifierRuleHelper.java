package com.axialeaa.doormat.helper;

import net.minecraft.block.Block;
import net.minecraft.registry.Registries;
import net.minecraft.util.Identifier;

import java.util.Optional;

public class BlockIdentifierRuleHelper {

    public static Optional<Block> getBlock(String id) {
        Identifier id2 = Identifier.tryParse(id);
        return id2 == null ? Optional.empty() : Optional.of(Registries.BLOCK.get(id2));
    }

}
