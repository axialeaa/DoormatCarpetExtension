package com.axialeaa.doormat.mixin.rule.compactTooltips;

import carpet.utils.Translations;
import com.axialeaa.doormat.DoormatSettings;
import com.llamalad7.mixinextras.injector.v2.WrapWithCondition;
import com.llamalad7.mixinextras.sugar.Local;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.block.DecoratedPotBlock;
import net.minecraft.block.entity.Sherds;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.item.tooltip.TooltipType;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.Collections;
import java.util.List;
import java.util.function.Consumer;
import java.util.stream.Stream;

@Environment(EnvType.CLIENT)
@Mixin(DecoratedPotBlock.class)
public class DecoratedPotBlockMixin {

    @WrapWithCondition(method = "appendTooltip", at = @At(value = "INVOKE", target = "Ljava/util/List;add(Ljava/lang/Object;)Z", ordinal = 0))
    private boolean shouldApplyEmptyLine(List<Text> instance, Object e) {
        return !DoormatSettings.compactPotTooltips.enabled();
    }

    /**
     * If the rule is enabled, this compresses consecutive lines of the pot tooltip, changing it to
     * <pre>Heart x4</pre>
     * instead of
     * <pre>Heart Pottery Sherd<br>Heart Pottery Sherd<br>Heart Pottery Sherd<br>Heart Pottery Sherd</pre>
     * while totally ignoring bricks if the rule specifies it, otherwise showing them as "Blank".
     */
    @Inject(method = "appendTooltip", at = @At(value = "INVOKE", target = "Ljava/util/stream/Stream;of([Ljava/lang/Object;)Ljava/util/stream/Stream;", shift = At.Shift.BEFORE))
    private void addLines(ItemStack stack, Item.TooltipContext context, List<Text> tooltip, TooltipType options, CallbackInfo ci, @Local Sherds sherds) {
        if (!DoormatSettings.compactPotTooltips.enabled())
            return;

        Stream<Item> stream = sherds.stream().stream(); // for some reason they named the method to turn the sherds into a list "stream"...

        for (Item sherd : stream.distinct().toList()) { // iterates through a list of distinct items
            int count = Collections.frequency(sherds.stream(), sherd); // and finds the quantity of this item in the original list

            if (DoormatSettings.compactPotTooltips == DoormatSettings.PotTooltipMode.IGNORE_BRICKS && sherd == Items.BRICK)
                return;

            String translate = Translations.tr("compact_tooltip.pot." + sherd.getTranslationKey().replace("item.minecraft.", ""));
            tooltip.add(Text.literal(count > 1 ? translate + " x" + count : translate) // add a numerical tag at the end if there are more than 1 of this type
                .formatted(Formatting.GRAY));
        }
    }

    @WrapWithCondition(method = "appendTooltip", at = @At(value = "INVOKE", target = "Ljava/util/stream/Stream;forEach(Ljava/util/function/Consumer;)V"))
    private boolean shouldIterateThroughSherds(Stream<Item> instance, Consumer<? super Item> consumer) {
        return !DoormatSettings.compactPotTooltips.enabled();
    }

}
