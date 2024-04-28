package com.axialeaa.doormat.mixin.tinker_kit.quasiconnectivity;

import carpet.CarpetSettings;
import carpet.api.settings.SettingsManager;
import com.llamalad7.mixinextras.injector.ModifyExpressionValue;
import com.llamalad7.mixinextras.sugar.Local;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

import java.lang.reflect.Field;
import java.util.Arrays;

/**
 * Many thanks to replaceitem for writing this up for me, unprompted!
 * <a href="https://discord.com/channels/882822986795716608/882833616072237056/1234119808585437234">Here's the link to the Discord message</a>
 */
@Mixin(SettingsManager.class)
public class CarpetSettingsMixin {

    @ModifyExpressionValue(method = "parseSettingsClass", at = @At(value = "INVOKE", target = "Ljava/lang/Class;getDeclaredFields()[Ljava/lang/reflect/Field;"), remap = false)
    private Field[] removeSettings(Field[] original, @Local(argsOnly = true) Class<?> settingsClass) {
        if (settingsClass != CarpetSettings.class)
            return original;

        return Arrays.stream(original).filter(field -> !field.getName().equals("quasiConnectivity")).toArray(Field[]::new);
    }

}
