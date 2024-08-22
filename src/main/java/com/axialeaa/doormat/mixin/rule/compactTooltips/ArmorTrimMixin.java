package com.axialeaa.doormat.mixin.rule.compactTooltips;

import carpet.utils.Translations;
import com.axialeaa.doormat.DoormatSettings;
import com.llamalad7.mixinextras.injector.v2.WrapWithCondition;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.item.trim.ArmorTrim;
import net.minecraft.item.trim.ArmorTrimMaterial;
import net.minecraft.item.trim.ArmorTrimPattern;
import net.minecraft.registry.entry.RegistryEntry;
import net.minecraft.screen.ScreenTexts;
import net.minecraft.text.Text;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyArg;
import org.spongepowered.asm.mixin.injection.Slice;

import java.util.function.Consumer;

@Environment(EnvType.CLIENT)
@Mixin(ArmorTrim.class)
public abstract class ArmorTrimMixin {

    @Shadow public abstract RegistryEntry<ArmorTrimMaterial> getMaterial();
    @Shadow public abstract RegistryEntry<ArmorTrimPattern> getPattern();

    /**
     * If the rule is enabled, this changes the armour trim tooltip to
     * <pre>Amethyst Coast Armor Trim</pre>
     * instead of
     * <pre>Upgrade: <br> Coast Armor Trim <br> Amethyst Material</pre>
     */
    @ModifyArg(method = "appendTooltip", at = @At(value = "INVOKE", target = "Ljava/util/function/Consumer;accept(Ljava/lang/Object;)V", ordinal = 0))
    private Object modifyUpgradeText(Object object) {
        if (!DoormatSettings.compactTrimTooltips.enabled())
            return object;

        ArmorTrimMaterial material = this.getMaterial().value();
        ArmorTrimPattern pattern = this.getPattern().value();
        String ingredient = Translations.tr("compact_tooltip.trim." + material.assetName());

        boolean onlyPattern = DoormatSettings.compactTrimTooltips == DoormatSettings.TrimTooltipMode.ONLY_PATTERN;

        return Text.literal(onlyPattern ? "": ingredient)
            .setStyle(material.description().getStyle())
            .append(onlyPattern ? Text.empty() : ScreenTexts.space())
            .append(pattern.description());
    }

    @WrapWithCondition(method = "appendTooltip", slice = @Slice(from = @At(value = "INVOKE", target = "Lnet/minecraft/screen/ScreenTexts;space()Lnet/minecraft/text/MutableText;", ordinal = 1)), at = @At(value = "INVOKE", target = "Ljava/util/function/Consumer;accept(Ljava/lang/Object;)V"))
    private boolean shouldApplyExtraLines(Consumer<Text> instance, Object o) {
        return !DoormatSettings.compactTrimTooltips.enabled();
    }


}
