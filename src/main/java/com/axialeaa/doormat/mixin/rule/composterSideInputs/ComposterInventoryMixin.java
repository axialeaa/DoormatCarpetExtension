package com.axialeaa.doormat.mixin.rule.composterSideInputs;

import com.axialeaa.doormat.setting.DoormatSettings;
import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.Direction;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

/**
 * This uses a target instead of a direct class reference due to being private, and the high priority is to fix incompatibility with Lithium's mixin.alloc.composter.
 */
@Mixin(targets = "net/minecraft/block/ComposterBlock$ComposterInventory", priority = 1500)
public class ComposterInventoryMixin {

    /**
     * When the rule is enabled, this changes dir == Direction.UP to dir == dir which always returns true, thus bypassing the direction check.
     */
    @WrapOperation(method = "canInsert", at = @At(value = "FIELD", target = "Lnet/minecraft/util/math/Direction;UP:Lnet/minecraft/util/math/Direction;"))
    private Direction allowInsertionFromAllSides(Operation<Direction> original, int slot, ItemStack stack, @Nullable Direction dir) {
        return DoormatSettings.composterSideInputs ? dir : Direction.UP;
    }

    /**
     * When the rule is enabled, this changes side == Direction.UP to side == side which always returns true, thus bypassing the direction check.
     */
    @WrapOperation(method = "getAvailableSlots", at = @At(value = "FIELD", target = "Lnet/minecraft/util/math/Direction;UP:Lnet/minecraft/util/math/Direction;"))
    private Direction forceSideAvailabilty(Operation<Direction> original, Direction side) {
        return DoormatSettings.composterSideInputs ? side : Direction.UP;
    }

}
