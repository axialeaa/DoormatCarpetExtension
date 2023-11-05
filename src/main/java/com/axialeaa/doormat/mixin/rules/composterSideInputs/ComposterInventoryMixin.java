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

    @ModifyReturnValue(method = "canInsert", at = @At("RETURN"))
    private boolean allowInsertionFromAllSides(boolean original, ItemStack stack, @Nullable Direction dir) {
        return DoormatSettings.composterSideInputs ?
            // if the rule is enabled...
            !this.dirty && ComposterBlock.ITEM_TO_LEVEL_INCREASE_CHANCE.containsKey(stack.getItem()) :
            // return true if the composter is not marked dirty and the item in question is valid for the composter
            original; // otherwise return true based on the same condition + a direction check, basically
    }

    @ModifyReturnValue(method = "getAvailableSlots", at = @At("RETURN"))
    private int[] forceSideAvailabilty(int[] original) {
        return DoormatSettings.composterSideInputs ? new int[]{0} : original;
    }

}
