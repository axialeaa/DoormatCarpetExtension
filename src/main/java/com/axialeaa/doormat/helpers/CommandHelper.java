package com.axialeaa.doormat.helpers;

import net.minecraft.resource.featuretoggle.FeatureFlags;
import net.minecraft.resource.featuretoggle.FeatureSet;
import net.minecraft.server.command.ServerCommandSource;

public class CommandHelper {

    public static boolean isExperimentalDatapackDisabled(ServerCommandSource source) {
        FeatureSet featureSet = source.getEnabledFeatures();
        return !featureSet.contains(FeatureFlags.UPDATE_1_21);
    }

}
