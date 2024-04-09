package com.axialeaa.doormat.mixin.rule.barrelItemDumping;

import net.minecraft.block.entity.BarrelBlockEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.util.collection.DefaultedList;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

@Mixin(BarrelBlockEntity.class)
public interface BarrelBlockEntityAccessor {

    @Accessor("inventory")
    DefaultedList<ItemStack> getInventory();

}
