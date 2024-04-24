package com.axialeaa.doormat;

import carpet.CarpetExtension;
import carpet.CarpetServer;
import carpet.utils.Translations;
import com.axialeaa.doormat.command.QuasiConnectivityCommand;
import com.axialeaa.doormat.command.RandomTickCommand;
import com.axialeaa.doormat.command.UpdateTypeCommand;
import com.axialeaa.doormat.tinker_kit.ConfigFile;
import com.axialeaa.doormat.tinker_kit.TinkerKitRegistry;
import com.axialeaa.doormat.util.UpdateType;
import com.mojang.brigadier.CommandDispatcher;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.loader.api.FabricLoader;
import net.fabricmc.loader.api.Version;
import net.fabricmc.loader.api.metadata.ModMetadata;
import net.minecraft.block.*;
import net.minecraft.command.CommandRegistryAccess;
import net.minecraft.resource.featuretoggle.FeatureFlags;
import net.minecraft.resource.featuretoggle.FeatureSet;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.command.ServerCommandSource;
import net.minecraft.server.world.ServerWorld;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Map;

public class DoormatServer implements ModInitializer, CarpetExtension {

	public static final String MOD_ID = "doormat";
	public static final String MOD_NAME;
	public static final Version MOD_VERSION;
    public static final Logger LOGGER = LoggerFactory.getLogger(MOD_ID);
	public static int MAX_QC_RANGE = 1;

	static {
		ModMetadata metadata = FabricLoader.getInstance().getModContainer(MOD_ID).orElseThrow(RuntimeException::new).getMetadata();

		MOD_NAME = metadata.getName();
		MOD_VERSION = metadata.getVersion();
	}

	public static final boolean IS_DEBUG = DoormatSettings.incrediblySecretSetting || FabricLoader.getInstance().isDevelopmentEnvironment();

	public static boolean hasExperimentalDatapack(MinecraftServer server) {
		FeatureSet enabledFeatures = server.getSaveProperties().getEnabledFeatures();
		return enabledFeatures.contains(FeatureFlags.UPDATE_1_21);
	}

	@Override
	public void onInitialize() {
		CarpetServer.manageExtension(new DoormatServer());
        LOGGER.info("{} initialized. Wipe your feet!", MOD_NAME);

		// Handles all instances of most blocks for automatic inter-mod compatibility.
		// Modders should not need to add their own variants manually if the block in question extends any of these classes.
		TinkerKitRegistry.putBlocksByClass(DoorBlock.class, 0, UpdateType.SHAPE);
		TinkerKitRegistry.putBlocksByClass(TrapdoorBlock.class, 0, UpdateType.SHAPE);
		TinkerKitRegistry.putBlocksByClass(FenceGateBlock.class, 0, UpdateType.SHAPE);
		TinkerKitRegistry.putBlocksByClass(AbstractSkullBlock.class, 0, UpdateType.SHAPE);
		TinkerKitRegistry.putBlocksByClass(AbstractRedstoneGateBlock.class, 0, UpdateType.SHAPE);
		TinkerKitRegistry.putBlocksByClass(DispenserBlock.class, 1, UpdateType.SHAPE);
		TinkerKitRegistry.putBlocksByClass(RedstoneLampBlock.class, 0, UpdateType.SHAPE);
		TinkerKitRegistry.putBlocksByClass(BulbBlock.class, 0, UpdateType.BOTH);
		TinkerKitRegistry.putBlocksByClass(PistonBlock.class, 1);
		TinkerKitRegistry.putBlocksByClass(TntBlock.class, 0, UpdateType.BOTH);
		TinkerKitRegistry.putBlocksByClass(NoteBlock.class, 0, UpdateType.BOTH);
		TinkerKitRegistry.putBlocksByClass(BellBlock.class, 0, UpdateType.BOTH);
		TinkerKitRegistry.putBlocksByClass(HopperBlock.class, 0, UpdateType.SHAPE);
		TinkerKitRegistry.putBlocksByClass(CommandBlock.class, 0);
		TinkerKitRegistry.putBlocksByClass(StructureBlock.class, 0);
		TinkerKitRegistry.putBlocksByClass(BarrelBlock.class, 0, UpdateType.SHAPE);

		TinkerKitRegistry.putBlock(Blocks.RAIL, UpdateType.BOTH);
		TinkerKitRegistry.putBlocks(0, UpdateType.BOTH,
			Blocks.ACTIVATOR_RAIL,
			Blocks.POWERED_RAIL
		);
		TinkerKitRegistry.putBlock(Blocks.CRAFTER, 0, UpdateType.SHAPE);

		TinkerKitRegistry.putBlock(Blocks.OBSERVER, 					                  UpdateType.SHAPE);
		TinkerKitRegistry.putBlock(Blocks.REDSTONE_WALL_TORCH, 			   0);
		TinkerKitRegistry.putBlock(Blocks.BIG_DRIPLEAF, 				   0, UpdateType.SHAPE);
		TinkerKitRegistry.putBlock(Blocks.BARREL, 						   0, UpdateType.SHAPE);
	}

	@Override
	public void onServerLoadedWorlds(MinecraftServer server) {
        for (ServerWorld world : server.getWorlds())
            MAX_QC_RANGE = Math.max(MAX_QC_RANGE, world.getHeight() - 1);

        ConfigFile.loadFromFile(server); // Loads all quasi-connectivity and update type values when the server starts.
		ConfigFile.updateFile(server); // Updates the config file when the server starts. Not technically necessary, but keeps things unambiguous.
	}

	@Override
	public void onGameStarted() {
		CarpetServer.settingsManager.parseSettingsClass(DoormatSettings.class);
		CarpetServer.settingsManager.registerRuleObserver((source, changedRule, userInput) -> DoormatRuleObservers.moreTimeArgumentUnits(changedRule));
	}

	@Override
	public void registerCommands(CommandDispatcher<ServerCommandSource> dispatcher, CommandRegistryAccess registryAccess) {
		RandomTickCommand.register(dispatcher);
		QuasiConnectivityCommand.register(dispatcher, registryAccess);
		UpdateTypeCommand.register(dispatcher, registryAccess);
	}

	@Override
	public void registerLoggers() {
		DoormatLoggers.register();
	}

	@Override
	public Map<String, String> canHasTranslations(String lang) {
		return Translations.getTranslationFromResourcePath("assets/" + MOD_ID + "/lang/%s.json".formatted(lang));
	}

}