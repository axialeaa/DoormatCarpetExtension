package com.axialeaa.doormat.settings.enum_options;

import net.minecraft.block.spawner.EntityDetector;

public enum EntityDetectorMode {

    SURVIVAL_PLAYERS      (EntityDetector.SURVIVAL_PLAYERS),
    NON_SPECTATOR_PLAYERS (EntityDetector.NON_SPECTATOR_PLAYERS),
    SHEEP                 (EntityDetector.SHEEP);

    public final EntityDetector detector;

    EntityDetectorMode(EntityDetector detector) {
        this.detector = detector;
    }

}