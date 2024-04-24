package com.axialeaa.doormat.mixin.rule.fireAspectLighting;

import com.axialeaa.doormat.helper.FireAspectLightingHelper;
import com.llamalad7.mixinextras.injector.ModifyReturnValue;
import net.minecraft.item.BlockItem;
import net.minecraft.item.Item;
import net.minecraft.item.ItemUsageContext;
import net.minecraft.util.ActionResult;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

/**
 * Mixing into {@link Item} and {@link BlockItem} accommodates using cheats or nbt editing to put fire aspect on non-tool items.
 */
@Mixin({
    Item.class,
    BlockItem.class
})
public class Item_BlockItemMixin {

    @ModifyReturnValue(method = "useOnBlock", at = @At("RETURN"))
    public ActionResult fireAspectLight(ActionResult original, ItemUsageContext ctx) {
        ActionResult actionResult = FireAspectLightingHelper.onUse(ctx);
        return actionResult == ActionResult.PASS ? original : actionResult;
    }

}
