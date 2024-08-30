package com.axialeaa.doormat.setting.enum_option;

import net.minecraft.block.spawner.EntityDetector;

public enum TrialSpawnerEntityRequirementMode {

    SURVIVAL_PLAYERS      (EntityDetector.SURVIVAL_PLAYERS),
    NON_SPECTATOR_PLAYERS (EntityDetector.NON_SPECTATOR_PLAYERS),
    SHEEP                 (EntityDetector.SHEEP);

    public final EntityDetector detector;

    TrialSpawnerEntityRequirementMode(EntityDetector detector) {
        this.detector = detector;
    }

}