package com.axialeaa.doormat.setting.condition;

import carpet.api.settings.Rule;
import com.axialeaa.doormat.Doormat;
import net.fabricmc.loader.api.FabricLoader;

public abstract class ModAbsenceCondition implements Rule.Condition {

    public abstract String getModId();

    public abstract String getModName();

    public abstract String getRuleName();

    @Override
    public boolean shouldRegister() {
        if (FabricLoader.getInstance().isModLoaded(this.getModId())) {
            Doormat.LOGGER.warn("{} detected! Bypassing registration of rule {}.", this.getModName(), this.getRuleName());
            return false;
        }

        return true;
    }

}
