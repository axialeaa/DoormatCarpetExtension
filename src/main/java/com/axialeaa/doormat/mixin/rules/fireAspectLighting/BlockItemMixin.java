package com.axialeaa.doormat.mixin.rules.fireAspectLighting;

import com.axialeaa.doormat.helpers.FireAspectLighting;
import net.minecraft.item.BlockItem;
import net.minecraft.item.ItemUsageContext;
import net.minecraft.util.ActionResult;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(BlockItem.class)
// mix directly into BlockItem.class to accommodate using cheats/nbt editing to put fire aspect on non-tool items
public class BlockItemMixin {

    @Inject(method = "useOnBlock", at = @At("HEAD"), cancellable = true)
    public void fireAspectLight(ItemUsageContext ctx, CallbackInfoReturnable<ActionResult> cir) {
        FireAspectLighting.lightOnUse(ctx, cir);
    }

}
