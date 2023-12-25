package com.axialeaa.doormat;

import carpet.CarpetExtension;
import carpet.CarpetServer;
import carpet.api.settings.CarpetRule;
import carpet.utils.Translations;
import com.axialeaa.doormat.command.QuasiConnectivityCommand;
import com.axialeaa.doormat.command.RandomTickCommand;
import com.axialeaa.doormat.command.UpdateTypeCommand;
import com.axialeaa.doormat.util.ConfigFile;
import com.axialeaa.doormat.util.RedstoneRule;
import com.mojang.brigadier.CommandDispatcher;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.command.CommandRegistryAccess;
import net.minecraft.resource.featuretoggle.FeatureFlags;
import net.minecraft.resource.featuretoggle.FeatureSet;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.command.ServerCommandSource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Map;

import static com.axialeaa.doormat.util.RedstoneRule.*;

public class DoormatServer implements ModInitializer, CarpetExtension {

	public static final String MOD_ID = "doormat";
	public static final String MOD_NAME = "Doormat";
    public static final Logger LOGGER = LoggerFactory.getLogger(MOD_ID);

	public static final boolean IS_DEBUG = DoormatSettings.incrediblySecretSetting || FabricLoader.getInstance().isDevelopmentEnvironment();

	public static boolean hasExperimentalDatapack(MinecraftServer server) {
		FeatureSet featureSet = server.getSaveProperties().getEnabledFeatures();
		return featureSet.contains(FeatureFlags.UPDATE_1_21);
	}

	@Override
	public void onInitialize() {
		CarpetServer.manageExtension(new DoormatServer());
		LOGGER.info(MOD_NAME + " initialized. Wipe your feet!");
	}

	@Override
	public void onServerLoadedWorlds(MinecraftServer server) {
		for (RedstoneRule component : values()) {
			if (!DoormatSettings.redstoneOpensBarrels && component == BARREL)
				continue;

			if (!hasExperimentalDatapack(server) && (component == COPPER_BULB || component == CRAFTER))
				continue;

			qcKeys.put(component.getKey(), component);
			if (component.getDefaultUpdateTypeValue() != null)
				updateTypeKeys.put(component.getKey(), component);
		}
		// Go through the list of components in the RedstoneRules enum, and assign their keys to the hashmaps.
		// This will be useful for command autocompletion later down the line.

		ConfigFile.load(server); // Loads all quasi-connectivity and update type values when the server starts.
	}

	@Override
	public void onGameStarted() {
		CarpetServer.settingsManager.parseSettingsClass(DoormatSettings.class);
		CarpetServer.settingsManager.registerRuleObserver(DoormatServer::amendKeyMapsForRule);
	}

	@Override
	public void registerCommands(CommandDispatcher<ServerCommandSource> dispatcher, CommandRegistryAccess commandBuildContext) {
		RandomTickCommand.register(dispatcher);
		QuasiConnectivityCommand.register(dispatcher);
		UpdateTypeCommand.register(dispatcher);
	}

	@Override
	public Map<String, String> canHasTranslations(String lang) {
		return Translations.getTranslationFromResourcePath("assets/" + MOD_ID + "/lang/%s.json".formatted(lang));
	}

	/**
	 * Removes "barrel" from the /quasiconnectivity and /updatetype key hashmaps when redstoneOpensBarrels is disabled, and adds it back when enabled.
	 */
	public static void amendKeyMapsForRule(ServerCommandSource serverCommandSource, CarpetRule<?> currentRuleState, String originalUserTest) {
		boolean settingState = currentRuleState.settingsManager().getCarpetRule("redstoneOpensBarrels").value().equals(true);
		String key = BARREL.getKey();

		if (settingState) {
			qcKeys.put(key, BARREL);
			updateTypeKeys.put(key, BARREL);
		}
		else {
			qcKeys.remove(key);
			updateTypeKeys.remove(key);
		}
	}

}