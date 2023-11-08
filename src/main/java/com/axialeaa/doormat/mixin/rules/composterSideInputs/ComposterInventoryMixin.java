package com.axialeaa.doormat.mixin.rules.composterSideInputs;

import com.axialeaa.doormat.DoormatSettings;
import com.llamalad7.mixinextras.injector.ModifyReturnValue;
import net.minecraft.block.ComposterBlock;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.Direction;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;

@Mixin(ComposterBlock.ComposterInventory.class)
public class ComposterInventoryMixin {

    @Shadow private boolean dirty;

    /**
     * @param original the default return value of canInsert()
     * @param stack the item stack to insert into the composter
     * @param dir the direction from where to insert
     * @return the original return value without the direction check if the rule is enabled, otherwise the default return value.
     */
    @ModifyReturnValue(method = "canInsert", at = @At("RETURN"))
    private boolean allowInsertionFromAllSides(boolean original, ItemStack stack, @Nullable Direction dir) {
        return DoormatSettings.composterSideInputs ?
            !this.dirty && ComposterBlock.ITEM_TO_LEVEL_INCREASE_CHANCE.containsKey(stack.getItem()) :
            original;
    }

    @ModifyReturnValue(method = "getAvailableSlots", at = @At("RETURN"))
    private int[] forceSideAvailabilty(int[] original) {
        return DoormatSettings.composterSideInputs ? new int[]{0} : original;
    }

}
