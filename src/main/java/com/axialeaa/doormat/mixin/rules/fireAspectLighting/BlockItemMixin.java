package com.axialeaa.doormat.mixin.rules.fireAspectLighting;

import com.axialeaa.doormat.helpers.FireAspectLighting;
import net.minecraft.item.BlockItem;
import net.minecraft.item.ItemUsageContext;
import net.minecraft.util.ActionResult;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

/**
 * Mixing into {@link BlockItem} accommodates using cheats or nbt editing to put fire aspect on non-tool items.
 */
@Mixin(BlockItem.class)
public class BlockItemMixin {

    @Inject(method = "useOnBlock", at = @At("HEAD"), cancellable = true)
    public void fireAspectLight(ItemUsageContext ctx, CallbackInfoReturnable<ActionResult> cir) {
        FireAspectLighting.lightOnUse(ctx, cir);
    }

}
