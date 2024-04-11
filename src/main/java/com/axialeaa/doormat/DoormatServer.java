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

		// Wooden Doors
		TinkerKitRegistry.putBlocks(0, UpdateType.SHAPE,
			Blocks.ACACIA_DOOR,
			Blocks.BAMBOO_DOOR,
			Blocks.BIRCH_DOOR,
			Blocks.CHERRY_DOOR,
			Blocks.DARK_OAK_DOOR,
			Blocks.JUNGLE_DOOR,
			Blocks.MANGROVE_DOOR,
			Blocks.OAK_DOOR,
			Blocks.SPRUCE_DOOR,
			Blocks.CRIMSON_DOOR,
			Blocks.WARPED_DOOR
		);
		// Metal Doors
		TinkerKitRegistry.putBlocks(0, UpdateType.SHAPE,
			Blocks.IRON_DOOR,
			Blocks.COPPER_DOOR,
			Blocks.EXPOSED_COPPER_DOOR,
			Blocks.WEATHERED_COPPER_DOOR,
			Blocks.OXIDIZED_COPPER_DOOR,
			Blocks.WAXED_COPPER_DOOR,
			Blocks.WAXED_EXPOSED_COPPER_DOOR,
			Blocks.WAXED_WEATHERED_COPPER_DOOR,
			Blocks.WAXED_OXIDIZED_COPPER_DOOR
		);
		// Wooden Trapdoors
		TinkerKitRegistry.putBlocks(0, UpdateType.SHAPE,
			Blocks.ACACIA_TRAPDOOR,
			Blocks.BAMBOO_TRAPDOOR,
			Blocks.BIRCH_TRAPDOOR,
			Blocks.CHERRY_TRAPDOOR,
			Blocks.DARK_OAK_TRAPDOOR,
			Blocks.JUNGLE_TRAPDOOR,
			Blocks.MANGROVE_TRAPDOOR,
			Blocks.OAK_TRAPDOOR,
			Blocks.SPRUCE_TRAPDOOR,
			Blocks.CRIMSON_TRAPDOOR,
			Blocks.WARPED_TRAPDOOR
		);
		// Metal Trapdoors
		TinkerKitRegistry.putBlocks(0, UpdateType.SHAPE,
			Blocks.IRON_TRAPDOOR,
			Blocks.COPPER_TRAPDOOR,
			Blocks.EXPOSED_COPPER_TRAPDOOR,
			Blocks.WEATHERED_COPPER_TRAPDOOR,
			Blocks.OXIDIZED_COPPER_TRAPDOOR,
			Blocks.WAXED_COPPER_TRAPDOOR,
			Blocks.WAXED_EXPOSED_COPPER_TRAPDOOR,
			Blocks.WAXED_WEATHERED_COPPER_TRAPDOOR,
			Blocks.WAXED_OXIDIZED_COPPER_TRAPDOOR
		);
		// Fence Gates
		TinkerKitRegistry.putBlocks(0, UpdateType.SHAPE,
			Blocks.ACACIA_FENCE_GATE,
			Blocks.BAMBOO_FENCE_GATE,
			Blocks.BIRCH_FENCE_GATE,
			Blocks.CHERRY_FENCE_GATE,
			Blocks.DARK_OAK_FENCE_GATE,
			Blocks.JUNGLE_FENCE_GATE,
			Blocks.MANGROVE_FENCE_GATE,
			Blocks.OAK_FENCE_GATE,
			Blocks.SPRUCE_FENCE_GATE,
			Blocks.CRIMSON_FENCE_GATE,
			Blocks.WARPED_FENCE_GATE
		);
		// Heads
		TinkerKitRegistry.putBlocks(0, UpdateType.SHAPE,
			Blocks.CREEPER_HEAD,
			Blocks.DRAGON_HEAD,
			Blocks.PIGLIN_HEAD,
			Blocks.PLAYER_HEAD,
			Blocks.SKELETON_SKULL,
			Blocks.WITHER_SKELETON_SKULL,
			Blocks.ZOMBIE_HEAD
		);
		// Wall Heads
		TinkerKitRegistry.putBlocks(0, UpdateType.SHAPE,
			// Note: wall heads have the same language translations as sitting heads, making them indistinguishable in command output.
			// I consider this to be out of my control (for now).
			Blocks.CREEPER_WALL_HEAD,
			Blocks.DRAGON_WALL_HEAD,
			Blocks.PIGLIN_WALL_HEAD,
			Blocks.PLAYER_WALL_HEAD,
			Blocks.SKELETON_WALL_SKULL,
			Blocks.WITHER_SKELETON_WALL_SKULL,
			Blocks.ZOMBIE_WALL_HEAD
		);
		// Copper Bulbs
		TinkerKitRegistry.putBlocks(0, UpdateType.BOTH,
			Blocks.COPPER_BULB,
			Blocks.EXPOSED_COPPER_BULB,
			Blocks.WEATHERED_COPPER_BULB,
			Blocks.OXIDIZED_COPPER_BULB,
			Blocks.WAXED_COPPER_BULB,
			Blocks.WAXED_EXPOSED_COPPER_BULB,
			Blocks.WAXED_WEATHERED_COPPER_BULB,
			Blocks.WAXED_OXIDIZED_COPPER_BULB
		);
		// Rails
		TinkerKitRegistry.putBlock(Blocks.RAIL, UpdateType.BOTH);
		TinkerKitRegistry.putBlocks(0, UpdateType.BOTH,
			Blocks.ACTIVATOR_RAIL,
			Blocks.POWERED_RAIL
		);
		// Pistons
		TinkerKitRegistry.putBlocks(1,
			Blocks.PISTON,
			Blocks.STICKY_PISTON
		);
		// Blocks That Fire Stuff
		TinkerKitRegistry.putBlock(Blocks.CRAFTER, 0, UpdateType.SHAPE);
		TinkerKitRegistry.putBlocks(1, UpdateType.SHAPE,
			Blocks.DISPENSER,
			Blocks.DROPPER
		);
		// Diodes
		TinkerKitRegistry.putBlocks(0, UpdateType.SHAPE,
			Blocks.REPEATER,
			Blocks.COMPARATOR
		);
		// Operator Blocks
		TinkerKitRegistry.putBlocks(0,
			Blocks.COMMAND_BLOCK,
			Blocks.REPEATING_COMMAND_BLOCK,
			Blocks.STRUCTURE_BLOCK
		);
		// Uncategorised
		TinkerKitRegistry.putBlock(Blocks.BARREL, 						   0, UpdateType.SHAPE);
		TinkerKitRegistry.putBlock(Blocks.BELL, 						   0, UpdateType.BOTH);
		TinkerKitRegistry.putBlock(Blocks.HOPPER, 						   0, UpdateType.SHAPE);
		TinkerKitRegistry.putBlock(Blocks.NOTE_BLOCK,    				   0, UpdateType.BOTH);
		TinkerKitRegistry.putBlock(Blocks.OBSERVER, 					                  UpdateType.SHAPE);
		TinkerKitRegistry.putBlock(Blocks.REDSTONE_LAMP, 				   0, UpdateType.SHAPE);
		TinkerKitRegistry.putBlock(Blocks.REDSTONE_WALL_TORCH, 			   0);
		TinkerKitRegistry.putBlock(Blocks.TNT, 							   0, UpdateType.BOTH);
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