package com.axialeaa.doormat.util;

import com.axialeaa.doormat.MainEntrypoint;
import net.minecraft.item.Item;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.registry.tag.TagKey;
import net.minecraft.util.Identifier;

public class DoormatTags {
    public static final TagKey<Item> EXPLOSION_IMMUNE_ITEMS = TagKey.of(RegistryKeys.ITEM, new Identifier(MainEntrypoint.MODID, "explosion_immune_items"));
}