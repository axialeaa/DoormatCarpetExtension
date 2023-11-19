package com.axialeaa.doormat.mixin.rules.compactPotTooltips;

import com.axialeaa.doormat.DoormatSettings;
import com.llamalad7.mixinextras.injector.WrapWithCondition;
import com.llamalad7.mixinextras.sugar.Local;
import net.minecraft.block.DecoratedPotBlock;
import net.minecraft.block.entity.DecoratedPotBlockEntity;
import net.minecraft.client.item.TooltipContext;
import net.minecraft.client.resource.language.I18n;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;
import net.minecraft.world.BlockView;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.Collections;
import java.util.List;

@Mixin(DecoratedPotBlock.class)
public class DecoratedPotBlockMixin {

    /**
     * Removes an empty line at the top of the pot tooltip when the rule is enabled.
     */
    @WrapWithCondition(method = "appendTooltip", at = @At(value = "INVOKE", target = "Ljava/util/List;add(Ljava/lang/Object;)Z", ordinal = 0))
    private boolean removeEmptyLine(List<Item> instance, Object o) {
        return !DoormatSettings.compactPotTooltips.enabled();
    }

    /**
     * If the rule is enabled, this compresses consecutive lines of the pot tooltip, changing it to
     * <pre>Heart x4</pre>
     * instead of
     * <pre>Heart Pottery Sherd<br>Heart Pottery Sherd<br>Heart Pottery Sherd<br>Heart Pottery Sherd</pre>
     * while totally ignoring bricks if the rule specifies it, otherwise showing them as "Blank".
     */
    @Inject(method = "appendTooltip", at = @At(value = "INVOKE", target = "Ljava/util/stream/Stream;of([Ljava/lang/Object;)Ljava/util/stream/Stream;", shift = At.Shift.BEFORE), cancellable = true)
    private void compressAndRename(ItemStack stack, @Nullable BlockView world, List<Text> tooltip, TooltipContext options, CallbackInfo ci, @Local DecoratedPotBlockEntity.Sherds sherds) {
        if (DoormatSettings.compactPotTooltips.enabled()) {
            ci.cancel();
            for (Item sherd : sherds.stream().distinct().toList()) {
                int count = Collections.frequency(sherds.stream().toList(), sherd);
                if (DoormatSettings.compactPotTooltips == DoormatSettings.PotTooltipMode.IGNORE_BRICKS && sherd == Items.BRICK)
                    continue; // if the sherd item is a brick and the rule says to ignore it, skip this iteration

                String translate = I18n.translate("pot." + sherd.getTranslationKey().replace("item.minecraft.", "") + ".compact");
                tooltip.add(Text
                    .literal(count > 1 ? translate + " x" + count : translate) // add a numerical tag at the end if there are more than 1 of this type
                    .formatted(Formatting.GRAY)); // set the colour
            }
        }
    }

}
