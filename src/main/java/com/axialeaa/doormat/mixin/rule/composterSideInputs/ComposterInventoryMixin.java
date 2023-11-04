package com.axialeaa.doormat.mixin.rule.composterSideInputs;

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

    @SuppressWarnings("unused")
    @ModifyReturnValue(method = "canInsert", at = @At("RETURN"))
    private boolean test(boolean original, int slot, ItemStack stack, @Nullable Direction dir) {
        return DoormatSettings.composterSideInputs ?
            !this.dirty && ComposterBlock.ITEM_TO_LEVEL_INCREASE_CHANCE.containsKey(stack.getItem()) :
            original;
    }

    @SuppressWarnings("unused")
    @ModifyReturnValue(method = "getAvailableSlots", at = @At("RETURN"))
    private int[] test2(int[] original, Direction side) {
        return DoormatSettings.composterSideInputs ? new int[]{0} : original;
    }

}
