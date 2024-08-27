package com.axialeaa.doormat.mixin.rule.forceGrassSpreading;

import com.axialeaa.doormat.helper.ForceGrassSpreadingHelper;
import com.llamalad7.mixinextras.sugar.Local;
import net.minecraft.block.BlockState;
import net.minecraft.item.BoneMealItem;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(BoneMealItem.class)
public class BoneMealItemMixin {

    @Inject(method = "useOnFertilizable", at = @At(value = "INVOKE", target = "Lnet/minecraft/block/BlockState;getBlock()Lnet/minecraft/block/Block;", shift = At.Shift.AFTER), cancellable = true)
    private static void convertDirtOnUse(ItemStack stack, World world, BlockPos pos, CallbackInfoReturnable<Boolean> cir, @Local BlockState state) {
        if (ForceGrassSpreadingHelper.onUse(world, pos, state))
            cir.setReturnValue(true);
    }

}
