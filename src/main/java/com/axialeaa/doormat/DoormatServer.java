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
import net.minecraft.block.Blocks;
import net.minecraft.command.CommandRegistryAccess;
import net.minecraft.resource.featuretoggle.FeatureFlags;
import net.minecraft.resource.featuretoggle.FeatureSet;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.command.ServerCommandSource;
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

		TinkerKitRegistry.putBlock(Blocks.ACACIA_DOOR, 					   0, UpdateType.SHAPE);
		TinkerKitRegistry.putBlock(Blocks.ACACIA_FENCE_GATE, 			   0, UpdateType.SHAPE);
		TinkerKitRegistry.putBlock(Blocks.ACACIA_TRAPDOOR, 				   0, UpdateType.SHAPE);
		TinkerKitRegistry.putBlock(Blocks.ACTIVATOR_RAIL, 				   0, UpdateType.BOTH);
		TinkerKitRegistry.putBlock(Blocks.BAMBOO_DOOR, 					   0, UpdateType.SHAPE);
		TinkerKitRegistry.putBlock(Blocks.BAMBOO_FENCE_GATE, 			   0, UpdateType.SHAPE);
		TinkerKitRegistry.putBlock(Blocks.BAMBOO_TRAPDOOR, 				   0, UpdateType.SHAPE);
		TinkerKitRegistry.putBlock(Blocks.BARREL, 						   0, UpdateType.SHAPE);
		TinkerKitRegistry.putBlock(Blocks.BELL, 						   0, UpdateType.BOTH);
		TinkerKitRegistry.putBlock(Blocks.BIRCH_DOOR, 					   0, UpdateType.SHAPE);
		TinkerKitRegistry.putBlock(Blocks.BIRCH_FENCE_GATE, 			   0, UpdateType.SHAPE);
		TinkerKitRegistry.putBlock(Blocks.BIRCH_TRAPDOOR, 				   0, UpdateType.SHAPE);
		TinkerKitRegistry.putBlock(Blocks.CHERRY_DOOR, 				       0, UpdateType.SHAPE);
		TinkerKitRegistry.putBlock(Blocks.CHERRY_FENCE_GATE, 			   0, UpdateType.SHAPE);
		TinkerKitRegistry.putBlock(Blocks.CHERRY_TRAPDOOR, 				   0, UpdateType.SHAPE);
		TinkerKitRegistry.putBlock(Blocks.COMMAND_BLOCK, 				   0);
		TinkerKitRegistry.putBlock(Blocks.COMPARATOR, 					   0, UpdateType.SHAPE);
		TinkerKitRegistry.putBlock(Blocks.COPPER_BULB, 					   0, UpdateType.BOTH);
		TinkerKitRegistry.putBlock(Blocks.COPPER_DOOR, 					   0, UpdateType.SHAPE);
		TinkerKitRegistry.putBlock(Blocks.COPPER_TRAPDOOR, 				   0, UpdateType.SHAPE);
		TinkerKitRegistry.putBlock(Blocks.CRAFTER, 						   0, UpdateType.SHAPE);
		TinkerKitRegistry.putBlock(Blocks.CREEPER_HEAD, 				   0, UpdateType.SHAPE);
		TinkerKitRegistry.putBlock(Blocks.CREEPER_WALL_HEAD, 			   0, UpdateType.SHAPE);
		TinkerKitRegistry.putBlock(Blocks.CRIMSON_DOOR, 			       0, UpdateType.SHAPE);
		TinkerKitRegistry.putBlock(Blocks.CRIMSON_FENCE_GATE, 			   0, UpdateType.SHAPE);
		TinkerKitRegistry.putBlock(Blocks.CRIMSON_TRAPDOOR, 			   0, UpdateType.SHAPE);
		TinkerKitRegistry.putBlock(Blocks.DARK_OAK_DOOR, 				   0, UpdateType.SHAPE);
		TinkerKitRegistry.putBlock(Blocks.DARK_OAK_FENCE_GATE, 			   0, UpdateType.SHAPE);
		TinkerKitRegistry.putBlock(Blocks.DARK_OAK_TRAPDOOR, 			   0, UpdateType.SHAPE);
		TinkerKitRegistry.putBlock(Blocks.DISPENSER, 					   1, UpdateType.SHAPE);
		TinkerKitRegistry.putBlock(Blocks.DRAGON_HEAD, 					   0, UpdateType.SHAPE);
		TinkerKitRegistry.putBlock(Blocks.DRAGON_WALL_HEAD, 			   0, UpdateType.SHAPE);
		TinkerKitRegistry.putBlock(Blocks.DROPPER, 						   1, UpdateType.SHAPE);
		TinkerKitRegistry.putBlock(Blocks.EXPOSED_COPPER_BULB, 			   0, UpdateType.BOTH);
		TinkerKitRegistry.putBlock(Blocks.EXPOSED_COPPER_DOOR, 			   0, UpdateType.SHAPE);
		TinkerKitRegistry.putBlock(Blocks.EXPOSED_COPPER_TRAPDOOR, 		   0, UpdateType.SHAPE);
		TinkerKitRegistry.putBlock(Blocks.HOPPER, 						   0, UpdateType.SHAPE);
		TinkerKitRegistry.putBlock(Blocks.IRON_DOOR, 					   0, UpdateType.SHAPE);
		TinkerKitRegistry.putBlock(Blocks.IRON_TRAPDOOR, 				   0, UpdateType.SHAPE);
		TinkerKitRegistry.putBlock(Blocks.JUNGLE_DOOR, 					   0, UpdateType.SHAPE);
		TinkerKitRegistry.putBlock(Blocks.JUNGLE_FENCE_GATE, 			   0, UpdateType.SHAPE);
		TinkerKitRegistry.putBlock(Blocks.JUNGLE_TRAPDOOR, 				   0, UpdateType.SHAPE);
		TinkerKitRegistry.putBlock(Blocks.MANGROVE_DOOR, 				   0, UpdateType.SHAPE);
		TinkerKitRegistry.putBlock(Blocks.MANGROVE_FENCE_GATE, 			   0, UpdateType.SHAPE);
		TinkerKitRegistry.putBlock(Blocks.MANGROVE_TRAPDOOR, 			   0, UpdateType.SHAPE);
		TinkerKitRegistry.putBlock(Blocks.NOTE_BLOCK,    				   0, UpdateType.BOTH);
		TinkerKitRegistry.putBlock(Blocks.OAK_DOOR, 				       0, UpdateType.SHAPE);
		TinkerKitRegistry.putBlock(Blocks.OAK_FENCE_GATE, 				   0, UpdateType.SHAPE);
		TinkerKitRegistry.putBlock(Blocks.OAK_TRAPDOOR, 			       0, UpdateType.SHAPE);
		TinkerKitRegistry.putBlock(Blocks.OBSERVER, 					                  UpdateType.SHAPE);
		TinkerKitRegistry.putBlock(Blocks.OXIDIZED_COPPER_BULB, 		   0, UpdateType.BOTH);
		TinkerKitRegistry.putBlock(Blocks.OXIDIZED_COPPER_DOOR, 		   0, UpdateType.SHAPE);
		TinkerKitRegistry.putBlock(Blocks.OXIDIZED_COPPER_TRAPDOOR, 	   0, UpdateType.SHAPE);
		TinkerKitRegistry.putBlock(Blocks.PIGLIN_HEAD, 				       0, UpdateType.SHAPE);
		TinkerKitRegistry.putBlock(Blocks.PIGLIN_WALL_HEAD, 			   0, UpdateType.SHAPE);
		TinkerKitRegistry.putBlock(Blocks.PISTON, 						   1);
		TinkerKitRegistry.putBlock(Blocks.PLAYER_HEAD, 				       0, UpdateType.SHAPE);
		TinkerKitRegistry.putBlock(Blocks.PLAYER_WALL_HEAD, 			   0, UpdateType.SHAPE);
		TinkerKitRegistry.putBlock(Blocks.POWERED_RAIL, 				   0, UpdateType.BOTH);
		TinkerKitRegistry.putBlock(Blocks.RAIL, 						        		  UpdateType.BOTH);
		TinkerKitRegistry.putBlock(Blocks.REDSTONE_LAMP, 				   0, UpdateType.SHAPE);
		TinkerKitRegistry.putBlock(Blocks.REDSTONE_WALL_TORCH, 			   0);
		TinkerKitRegistry.putBlock(Blocks.REPEATER, 					   0, UpdateType.SHAPE);
		TinkerKitRegistry.putBlock(Blocks.REPEATING_COMMAND_BLOCK, 		   0);
		TinkerKitRegistry.putBlock(Blocks.SKELETON_SKULL, 				   0, UpdateType.SHAPE);
		TinkerKitRegistry.putBlock(Blocks.SKELETON_WALL_SKULL, 			   0, UpdateType.SHAPE);
		TinkerKitRegistry.putBlock(Blocks.SPRUCE_DOOR, 				       0, UpdateType.SHAPE);
		TinkerKitRegistry.putBlock(Blocks.SPRUCE_FENCE_GATE, 			   0, UpdateType.SHAPE);
		TinkerKitRegistry.putBlock(Blocks.SPRUCE_TRAPDOOR, 				   0, UpdateType.SHAPE);
		TinkerKitRegistry.putBlock(Blocks.STICKY_PISTON, 				   1);
		TinkerKitRegistry.putBlock(Blocks.STRUCTURE_BLOCK, 				   1);
		TinkerKitRegistry.putBlock(Blocks.TNT, 							   0, UpdateType.BOTH);
		TinkerKitRegistry.putBlock(Blocks.WARPED_DOOR, 					   0, UpdateType.SHAPE);
		TinkerKitRegistry.putBlock(Blocks.WARPED_FENCE_GATE, 			   0, UpdateType.SHAPE);
		TinkerKitRegistry.putBlock(Blocks.WARPED_TRAPDOOR, 				   0, UpdateType.SHAPE);
		TinkerKitRegistry.putBlock(Blocks.WAXED_COPPER_BULB, 			   0, UpdateType.BOTH);
		TinkerKitRegistry.putBlock(Blocks.WAXED_COPPER_DOOR, 			   0, UpdateType.SHAPE);
		TinkerKitRegistry.putBlock(Blocks.WAXED_COPPER_TRAPDOOR, 		   0, UpdateType.SHAPE);
		TinkerKitRegistry.putBlock(Blocks.WAXED_EXPOSED_COPPER_BULB, 	   0, UpdateType.BOTH);
		TinkerKitRegistry.putBlock(Blocks.WAXED_EXPOSED_COPPER_DOOR, 	   0, UpdateType.SHAPE);
		TinkerKitRegistry.putBlock(Blocks.WAXED_EXPOSED_COPPER_TRAPDOOR,   0, UpdateType.SHAPE);
		TinkerKitRegistry.putBlock(Blocks.WAXED_OXIDIZED_COPPER_BULB,	   0, UpdateType.BOTH);
		TinkerKitRegistry.putBlock(Blocks.WAXED_OXIDIZED_COPPER_DOOR, 	   0, UpdateType.SHAPE);
		TinkerKitRegistry.putBlock(Blocks.WAXED_OXIDIZED_COPPER_TRAPDOOR,  0, UpdateType.SHAPE);
		TinkerKitRegistry.putBlock(Blocks.WAXED_WEATHERED_COPPER_BULB,     0, UpdateType.BOTH);
		TinkerKitRegistry.putBlock(Blocks.WAXED_WEATHERED_COPPER_DOOR,     0, UpdateType.SHAPE);
		TinkerKitRegistry.putBlock(Blocks.WAXED_WEATHERED_COPPER_TRAPDOOR, 0, UpdateType.SHAPE);
		TinkerKitRegistry.putBlock(Blocks.WEATHERED_COPPER_BULB,           0, UpdateType.BOTH);
		TinkerKitRegistry.putBlock(Blocks.WEATHERED_COPPER_DOOR, 		   0, UpdateType.SHAPE);
		TinkerKitRegistry.putBlock(Blocks.WEATHERED_COPPER_TRAPDOOR,       0, UpdateType.SHAPE);
		TinkerKitRegistry.putBlock(Blocks.WITHER_SKELETON_SKULL, 		   0, UpdateType.SHAPE);
		TinkerKitRegistry.putBlock(Blocks.WITHER_SKELETON_WALL_SKULL,	   0, UpdateType.SHAPE);
		TinkerKitRegistry.putBlock(Blocks.ZOMBIE_HEAD, 				       0, UpdateType.SHAPE);
		TinkerKitRegistry.putBlock(Blocks.ZOMBIE_WALL_HEAD, 			   0, UpdateType.SHAPE);
	}

	@Override
	public void onServerLoadedWorlds(MinecraftServer server) {
		server.getWorlds().forEach(world ->
			MAX_QC_RANGE = Math.max(MAX_QC_RANGE, world.getHeight() - 1));

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