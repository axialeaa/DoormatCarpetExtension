package com.axialeaa.doormat.mixin.rules.fireAspectLighting;

import com.axialeaa.doormat.DoormatSettings;
import com.axialeaa.doormat.helpers.FireAspectLightingHelper;
import net.minecraft.item.BlockItem;
import net.minecraft.item.Item;
import net.minecraft.item.ItemUsageContext;
import net.minecraft.util.ActionResult;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

/**
 * Mixing into {@link Item} and {@link BlockItem} accommodates using cheats or nbt editing to put fire aspect on non-tool items.
 */
@Mixin({Item.class, BlockItem.class})
public class Item_BlockItemMixin {

    @Inject(method = "useOnBlock", at = @At("HEAD"), cancellable = true)
    public void fireAspectLight(ItemUsageContext ctx, CallbackInfoReturnable<ActionResult> cir) {
        if (DoormatSettings.fireAspectLighting)
            cir.setReturnValue(FireAspectLightingHelper.light(ctx));
    }

}
