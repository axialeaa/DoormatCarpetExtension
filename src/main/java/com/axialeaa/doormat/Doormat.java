package com.axialeaa.doormat;

import carpet.CarpetExtension;
import carpet.CarpetServer;
import carpet.utils.Translations;
import com.axialeaa.doormat.command.RandomTickCommand;
import com.axialeaa.doormat.registry.DoormatLoggers;
import com.axialeaa.doormat.setting.DoormatSettings;
import com.axialeaa.doormat.tinker_kit.TinkerKitConfig;
import com.axialeaa.doormat.tinker_kit.TinkerKitInitializer;
import com.axialeaa.doormat.tinker_kit.TinkerType;
import com.mojang.brigadier.CommandDispatcher;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.loader.api.FabricLoader;
import net.fabricmc.loader.api.Version;
import net.fabricmc.loader.api.metadata.ModMetadata;
import net.minecraft.command.CommandRegistryAccess;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.command.ServerCommandSource;
import net.minecraft.util.Identifier;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import java.util.Map;

public class Doormat implements ModInitializer, CarpetExtension {

	public static final String MOD_ID = "doormat";

	public static final FabricLoader LOADER = FabricLoader.getInstance();
	private static final ModMetadata METADATA = LOADER.getModContainer(MOD_ID).orElseThrow(RuntimeException::new).getMetadata();

	public static final String MOD_NAME = METADATA.getName();
	public static final Version MOD_VERSION = METADATA.getVersion();

	public static final Logger LOGGER = LoggerFactory.getLogger(MOD_NAME);

	public static final String TRANSLATION_PATH = "assets/%s/lang/%s.json";
	public static final String TINKER_KIT = "tinker_kit";

	public static final boolean IS_DEBUG = DoormatSettings.incrediblySecretSetting || LOADER.isDevelopmentEnvironment();

	@Override
	public void onInitialize() {
		LOGGER.info("{} initialized. Wipe your feet!", MOD_NAME);
		CarpetServer.manageExtension(new Doormat());

		LOADER.getEntrypoints(TINKER_KIT, TinkerKitInitializer.class).forEach(initializer -> {
			initializer.registerTypes();
			initializer.registerBlocks();
			initializer.registerModificationPredicates();

			LOGGER.info("Registered Tinker Kit(s)!");
		});
	}

	@Override
	public void onServerLoadedWorlds(MinecraftServer server) {
        TinkerKitConfig.loadFromFile(server); // Loads all quasi-connectivity and update type values when the server starts.
		TinkerKitConfig.updateFile(server); // Updates the config file when the server starts. Not technically necessary, but keeps things unambiguous.
	}

	@Override
	public void onGameStarted() {
		CarpetServer.settingsManager.parseSettingsClass(DoormatSettings.class);
		CarpetServer.settingsManager.registerRuleObserver((source, changedRule, userInput) -> DoormatRuleObservers.moreTimeArgumentUnits(changedRule));
	}

	@Override
	public void registerCommands(CommandDispatcher<ServerCommandSource> dispatcher, CommandRegistryAccess registryAccess) {
		RandomTickCommand.register(dispatcher);

		for (TinkerType<?, ?> tinkerType : TinkerType.TYPES)
			tinkerType.command.register(dispatcher, registryAccess);
	}

	@Override
	public void registerLoggers() {
		DoormatLoggers.register();
	}

	@Override
	public Map<String, String> canHasTranslations(String lang) {
		String path = TRANSLATION_PATH.formatted(MOD_ID, lang);
		return Translations.getTranslationFromResourcePath(path);
	}

	public static Identifier id(String name) {
		return Identifier.of(MOD_ID, name);
	}

}