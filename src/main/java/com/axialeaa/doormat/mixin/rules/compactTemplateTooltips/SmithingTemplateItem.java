package com.axialeaa.doormat.mixin.rules.compactTemplateTooltips;

import com.axialeaa.doormat.DoormatSettings;
import com.llamalad7.mixinextras.injector.WrapWithCondition;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Slice;
import org.w3c.dom.Text;

import java.util.List;

@Mixin(net.minecraft.item.SmithingTemplateItem.class)
public class SmithingTemplateItem {

    /**
     * If the rule is enabled, this removes many unnecessary lines of the smithing table tooltip, changing it to
     * <pre>Coast Armor Trim</pre>
     * instead of
     * <pre>Coast Armor Trim<br><br>Applies to:<br> Armor<br>Ingredients:<br> Ingots & Crystals</pre>
     */
    @WrapWithCondition(method = "appendTooltip", slice = @Slice(from = @At(value = "INVOKE", target = "Ljava/util/List;add(Ljava/lang/Object;)Z", ordinal = 1)), at = @At(value = "INVOKE", target = "Ljava/util/List;add(Ljava/lang/Object;)Z"))
    private boolean removeLines(List<Text> instance, Object o) {
        return !DoormatSettings.compactTemplateTooltips;
    }

}
