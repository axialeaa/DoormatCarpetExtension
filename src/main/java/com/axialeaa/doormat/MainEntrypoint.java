package com.axialeaa.doormat;

import carpet.CarpetExtension;
import carpet.CarpetServer;
import carpet.api.settings.SettingsManager;
import carpet.utils.Translations;
import com.axialeaa.doormat.command.IdentityCommand;
import com.mojang.brigadier.CommandDispatcher;
import net.fabricmc.api.ModInitializer;
import net.minecraft.command.CommandRegistryAccess;
import net.minecraft.server.command.ServerCommandSource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Map;

public class MainEntrypoint implements ModInitializer, CarpetExtension {

	public static final String MODID = "doormat";
    public static final Logger LOGGER = LoggerFactory.getLogger(MODID);

	private static final SettingsManager doormatSettingsManager = new SettingsManager("1.0.0", MODID,"Axia's Doormat");

	@Override
	public void onInitialize() {
		CarpetServer.manageExtension(new MainEntrypoint());
		LOGGER.info(MODID + " initialized!");
	}

	@Override
	public void onGameStarted() {
		CarpetServer.settingsManager.parseSettingsClass(DoormatSettings.class);
	}

	@Override
	public SettingsManager extensionSettingsManager() {
		return doormatSettingsManager;
	}

	@Override
	public Map<String, String> canHasTranslations(String lang) {
		return Translations.getTranslationFromResourcePath("assets/doormat/lang/%s.json".formatted(lang));
	}

	@Override
	public void registerCommands(CommandDispatcher<ServerCommandSource> dispatcher, final CommandRegistryAccess commandBuildContext) {
		IdentityCommand.register(dispatcher);
	}

}