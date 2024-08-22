package com.axialeaa.doormat.helper;

import com.axialeaa.doormat.DoormatSettings;
import net.minecraft.block.Block;
import net.minecraft.entity.ItemEntity;
import net.minecraft.item.Item;

/**
 * Helper class for consistentExplosionImmunity logic, with help from <a href="https://github.com/lntricate1">intricate</a>.
 */
public class ConsistentExplosionImmunityHelper {

    /**
     * @param itemEntity the item entity to deal damage to
     * @param power the power/blast radius of the explosion
     * @return whether an explosion created inside this block (if it were placed) would destroy it.
     */
    public static boolean shouldDamage(ItemEntity itemEntity, float power) {
        if (!DoormatSettings.consistentExplosionImmunity)
            return true;

        Item item = itemEntity.getStack().getItem();
        float blastRes = Block.getBlockFromItem(item).getBlastResistance();

        return power * 1.3 > (blastRes + 0.3) * 0.3;
    }

}
