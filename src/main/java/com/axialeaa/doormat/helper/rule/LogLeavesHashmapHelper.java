package com.axialeaa.doormat.helper.rule;

import com.google.common.collect.Maps;
import net.minecraft.block.Block;
import net.minecraft.block.Blocks;
import net.minecraft.util.Util;

import java.util.List;
import java.util.Map;

public class LogLeavesHashmapHelper {

    public static final Map<Block, List<Block>> MAP = Util.make(Maps.newHashMap(), map -> {
        map.put(Blocks.OAK_LEAVES, List.of(Blocks.OAK_LOG, Blocks.STRIPPED_OAK_LOG, Blocks.OAK_WOOD, Blocks.STRIPPED_OAK_WOOD));
        map.put(Blocks.SPRUCE_LEAVES, List.of(Blocks.SPRUCE_LOG, Blocks.STRIPPED_SPRUCE_LOG, Blocks.SPRUCE_WOOD, Blocks.STRIPPED_SPRUCE_WOOD));
        map.put(Blocks.BIRCH_LEAVES, List.of(Blocks.BIRCH_LOG, Blocks.STRIPPED_BIRCH_LOG, Blocks.BIRCH_WOOD, Blocks.STRIPPED_BIRCH_WOOD));
        map.put(Blocks.ACACIA_LEAVES, List.of(Blocks.ACACIA_LOG, Blocks.STRIPPED_ACACIA_LOG, Blocks.ACACIA_WOOD, Blocks.STRIPPED_ACACIA_WOOD));
        map.put(Blocks.JUNGLE_LEAVES, List.of(Blocks.JUNGLE_LOG, Blocks.STRIPPED_JUNGLE_LOG, Blocks.JUNGLE_WOOD, Blocks.STRIPPED_JUNGLE_WOOD));
        map.put(Blocks.DARK_OAK_LEAVES, List.of(Blocks.DARK_OAK_LOG, Blocks.STRIPPED_DARK_OAK_LOG, Blocks.DARK_OAK_WOOD, Blocks.STRIPPED_DARK_OAK_WOOD));
        map.put(Blocks.MANGROVE_LEAVES, List.of(Blocks.MANGROVE_LOG, Blocks.STRIPPED_MANGROVE_LOG, Blocks.MANGROVE_WOOD, Blocks.STRIPPED_MANGROVE_WOOD));
        map.put(Blocks.CHERRY_LEAVES, List.of(Blocks.CHERRY_LOG, Blocks.STRIPPED_CHERRY_LOG, Blocks.CHERRY_WOOD, Blocks.STRIPPED_CHERRY_WOOD));
        map.put(Blocks.AZALEA_LEAVES, List.of(Blocks.OAK_LOG, Blocks.STRIPPED_OAK_LOG, Blocks.OAK_WOOD, Blocks.STRIPPED_OAK_WOOD));
        map.put(Blocks.FLOWERING_AZALEA_LEAVES, List.of(Blocks.OAK_LOG, Blocks.STRIPPED_OAK_LOG, Blocks.OAK_WOOD, Blocks.STRIPPED_OAK_WOOD));
    });

}
