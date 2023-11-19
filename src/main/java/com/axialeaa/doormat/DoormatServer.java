package com.axialeaa.doormat;

import carpet.CarpetExtension;
import carpet.CarpetServer;
import carpet.utils.Translations;
import com.axialeaa.doormat.command.DoormatCommands;
import net.fabricmc.api.ModInitializer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Map;

public class DoormatServer implements ModInitializer, CarpetExtension {

	public static final String MODID = "doormat";
	public static final String MODNAME = "Axia's Doormat";
    public static final Logger LOGGER = LoggerFactory.getLogger(MODID);

	@Override
	public void onInitialize() {
		CarpetServer.manageExtension(new DoormatServer());
		DoormatCommands.registerCommands();
		LOGGER.info(MODNAME + " initialized. Wipe your feet!");
	}

	@Override
	public void onGameStarted() {
		CarpetServer.settingsManager.parseSettingsClass(DoormatSettings.class);
	}

	@Override
	public Map<String, String> canHasTranslations(String lang) {
		return Translations.getTranslationFromResourcePath("assets/" + MODID + "/lang/%s.json".formatted(lang));
	}

}