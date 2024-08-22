package com.axialeaa.doormat.mixin.rule.compactTooltips;

import com.axialeaa.doormat.DoormatSettings;
import com.axialeaa.doormat.helper.CompactTooltipHelper;
import com.llamalad7.mixinextras.sugar.Local;
import net.minecraft.component.type.BannerPatternsComponent;
import net.minecraft.item.BannerItem;
import net.minecraft.item.ItemStack;
import net.minecraft.text.MutableText;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.List;

@Mixin(BannerItem.class)
public class BannerItemMixin {

    @Inject(method = "appendBannerTooltip", at = @At(value = "INVOKE", target = "Ljava/lang/Math;min(II)I", shift = At.Shift.BEFORE), cancellable = true)
    private static void appendBannerTooltipCarousel(ItemStack stack, List<Text> tooltip, CallbackInfo ci, @Local BannerPatternsComponent bannerPatternsComponent) {
        List<BannerPatternsComponent.Layer> layers = bannerPatternsComponent.layers();

        if (!DoormatSettings.compactBannerTooltips.enabled() || layers.isEmpty())
            return;

        CompactTooltipHelper.LIST_SIZE = layers.size();

        BannerPatternsComponent.Layer layer = layers.get(CompactTooltipHelper.LIST_INDEX);
        MutableText name = layer.getTooltipText().formatted(Formatting.GRAY);

        if (CompactTooltipHelper.LIST_SIZE == 1) {
            tooltip.add(name);
            ci.cancel();
        }

        switch (DoormatSettings.compactBannerTooltips) {
            case TRUE -> tooltip.add(CompactTooltipHelper.getFormattedFraction().append(name));
            case BAR -> {
                tooltip.add(name);
                MutableText mutableText = Text.empty();

                // Constructs the bar a number of characters long equal to the length of the enchantment list,
                // filling in the square if the banner pattern at that ordinal is currently displayed by the carousel
                for (int i = 1; i <= CompactTooltipHelper.getDenominator(); ++i) {
                    String dot = i == CompactTooltipHelper.getNumerator() ? "■" : "□";
                    mutableText.append(CompactTooltipHelper.format(Text.literal(dot)));
                }

                tooltip.add(CompactTooltipHelper.format(mutableText.append(" (" + CompactTooltipHelper.getFraction() + ")")));
            }
        }

        ci.cancel();
    }

}
