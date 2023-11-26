package com.axialeaa.doormat.mixin.rules.compactTrimTooltips;

import com.axialeaa.doormat.DoormatSettings;
import com.llamalad7.mixinextras.sugar.Local;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.resource.language.I18n;
import net.minecraft.item.ItemStack;
import net.minecraft.item.trim.ArmorTrim;
import net.minecraft.item.trim.ArmorTrimMaterial;
import net.minecraft.item.trim.ArmorTrimPattern;
import net.minecraft.registry.DynamicRegistryManager;
import net.minecraft.screen.ScreenTexts;
import net.minecraft.text.Text;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.List;

@Environment(EnvType.CLIENT)
@Mixin(ArmorTrim.class)
public class ArmorTrimMixin {

    /**
     * If the rule is enabled, this changes the armour trim tooltip to
     * <pre>Amethyst Coast Armor Trim</pre>
     * instead of
     * <pre>Upgrade: <br> Coast Armor Trim <br> Amethyst Material</pre>
     */
    @Inject(method = "appendTooltip", at = @At(value = "INVOKE", target = "Ljava/util/List;add(Ljava/lang/Object;)Z", ordinal = 0, shift = At.Shift.BEFORE), cancellable = true)
    private static void replaceWithCompact(ItemStack stack, DynamicRegistryManager registryManager, List<Text> tooltip, CallbackInfo ci, @Local ArmorTrim armorTrim) {
        if (DoormatSettings.compactTrimTooltips.enabled()) {
            ci.cancel();
            ArmorTrimMaterial material = armorTrim.getMaterial().value();
            ArmorTrimPattern pattern = armorTrim.getPattern().value();
            String ingredient = I18n.translate("trim_material." + material.assetName() + ".compact");
            if (DoormatSettings.compactTrimTooltips == DoormatSettings.TrimTooltipMode.ONLY_PATTERN)
                tooltip.add(Text
                    .literal("")
                    .setStyle(material.description().getStyle())
                    .append(pattern.description()));
            else
                tooltip.add(Text
                    .literal(ingredient) // add the material name
                    .setStyle(material.description().getStyle()) // set the colour of the text
                    .append(ScreenTexts.space())
                    .append(pattern.description())); // "____ Armor Trim"
        }
    }

}
