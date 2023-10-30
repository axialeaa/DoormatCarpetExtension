package com.axialeaa.doormat.helpers;

import com.axialeaa.doormat.DoormatSettings;
import net.minecraft.entity.Entity;
import net.minecraft.entity.ItemEntity;
import net.minecraft.item.BlockItem;
import net.minecraft.item.Item;

public class ConsistentExplosionImmunity {

    public static boolean disableDamageIfResistant(Entity entity, float power) {
        if (DoormatSettings.consistentExplosionImmunity && entity instanceof ItemEntity itemEntity) {
            Item item = itemEntity.getStack().getItem();
            if (item instanceof BlockItem blockItem) {
                float blastRes = blockItem.getBlock().getBlastResistance();
                return power * 1.3 > (blastRes + 0.3) * 0.3;
                // thanks to intricate for showing me this formula <3
                // the boolean result is equivalent to whether an explosion created inside this block would destroy it
            }
        }
        return true;
    }

}
