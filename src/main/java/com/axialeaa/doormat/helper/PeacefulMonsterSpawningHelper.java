package com.axialeaa.doormat.helper;

import com.axialeaa.doormat.setting.DoormatSettings;
import net.minecraft.world.Difficulty;

public class PeacefulMonsterSpawningHelper {

    /**
     * This works because we replace the invocation of world.getDifficulty() (or similar) with null.
     * As a result, the check performed is null != Difficulty.PEACEFUL which always returns true.
     * @implNote In some circumstances, this method is used for replacing world.getDifficulty() == Difficulty.PEACEFUL with world.getDifficulty() == null.
     */
    public static Difficulty bypassCheck(Difficulty difficulty) {
        return DoormatSettings.peacefulMonsterSpawning.isEnabled() ? null : difficulty;
    }

}
