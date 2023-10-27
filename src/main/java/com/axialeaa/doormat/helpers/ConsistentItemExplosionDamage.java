package com.axialeaa.doormat.helpers;

import net.minecraft.entity.Entity;
import net.minecraft.entity.ItemEntity;
import net.minecraft.item.BlockItem;
import net.minecraft.item.Item;

public class ConsistentItemExplosionDamage {

    public static boolean disableDamageIfResistant(boolean rule, Entity entity, float power) {
        if (rule && entity instanceof ItemEntity itemEntity) {
            Item item = itemEntity.getStack().getItem();
            if (item instanceof BlockItem blockItem) {
                float blastRes = blockItem.getBlock().getBlastResistance();
                return power * 1.3 > (blastRes + 0.3) * 0.3; // thanks to intricate for showing me this formula <3
            }
        }
        return true;
    }

}
