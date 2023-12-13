package com.axialeaa.doormat.mixin.rules.composterSideInputs;

import com.axialeaa.doormat.DoormatSettings;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.Direction;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

/**
 * This uses a target instead of a direct class reference, due to being private.
 */
@Mixin(targets = "net/minecraft/block/ComposterBlock$ComposterInventory")
public class ComposterInventoryMixin {

    /**
     * When the rule is enabled, this changes dir == Direction.UP to dir == dir which always returns true, thus bypassing the direction check.
     */
    @Redirect(method = "canInsert", at = @At(value = "FIELD", target = "Lnet/minecraft/util/math/Direction;UP:Lnet/minecraft/util/math/Direction;"))
    private Direction allowInsertionFromAllSides(int slot, ItemStack stack, @Nullable Direction dir) {
        return DoormatSettings.composterSideInputs ? dir : Direction.UP;
    }

    /**
     * When the rule is enabled, this changes side == Direction.UP to side == side which always returns true, thus bypassing the direction check.
     */
    @Redirect(method = "getAvailableSlots", at = @At(value = "FIELD", target = "Lnet/minecraft/util/math/Direction;UP:Lnet/minecraft/util/math/Direction;"))
    private Direction forceSideAvailabilty(Direction side) {
        return DoormatSettings.composterSideInputs ? side : Direction.UP;
    }

}
