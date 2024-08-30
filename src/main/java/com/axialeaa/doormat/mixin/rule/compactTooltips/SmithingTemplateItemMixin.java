package com.axialeaa.doormat.mixin.rule.compactTooltips;

import com.axialeaa.doormat.setting.DoormatSettings;
import com.llamalad7.mixinextras.injector.v2.WrapWithCondition;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.item.SmithingTemplateItem;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Slice;
import org.w3c.dom.Text;

import java.util.List;

@Environment(EnvType.CLIENT)
@Mixin(SmithingTemplateItem.class)
public class SmithingTemplateItemMixin {

    /**
     * If the rule is enabled, this removes many unnecessary lines of the smithing table tooltip, changing it to
     * <pre>Coast Armor Trim</pre>
     * instead of
     * <pre>Coast Armor Trim<br><br>Applies to:<br> Armor<br>Ingredients:<br> Ingots & Crystals</pre>
     */
    @WrapWithCondition(method = "appendTooltip", slice = @Slice(from = @At(value = "INVOKE", target = "Ljava/util/List;add(Ljava/lang/Object;)Z", ordinal = 1)), at = @At(value = "INVOKE", target = "Ljava/util/List;add(Ljava/lang/Object;)Z"))
    private boolean shouldApplyInfoLines(List<Text> instance, Object o) {
        return !DoormatSettings.compactTemplateTooltips;
    }

}
