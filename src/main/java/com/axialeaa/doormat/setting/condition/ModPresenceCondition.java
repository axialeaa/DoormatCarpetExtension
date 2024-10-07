package com.axialeaa.doormat.setting.condition;

import carpet.api.settings.Rule;
import com.axialeaa.doormat.Doormat;
import net.fabricmc.loader.api.FabricLoader;

public abstract class ModPresenceCondition implements Rule.Condition {

    public abstract String getModId();

    @Override
    public boolean shouldRegister() {
        if (!FabricLoader.getInstance().isModLoaded(this.getModId()))
            return true;

        Doormat.LOGGER.warn("Mod with ID {} detected! Bypassing registration of carpet rule.", this.getModId());
        return false;
    }

}
