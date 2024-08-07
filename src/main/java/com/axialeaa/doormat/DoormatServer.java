package com.axialeaa.doormat;

import carpet.CarpetExtension;
import carpet.CarpetServer;
import carpet.utils.Translations;
import com.axialeaa.doormat.command.*;
import com.axialeaa.doormat.tinker_kit.ConfigFile;
import com.axialeaa.doormat.tinker_kit.TinkerKit;
import com.axialeaa.doormat.util.UpdateType;
import com.mojang.brigadier.CommandDispatcher;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.loader.api.FabricLoader;
import net.fabricmc.loader.api.Version;
import net.fabricmc.loader.api.metadata.ModMetadata;
import net.minecraft.block.*;
import net.minecraft.command.CommandRegistryAccess;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.command.ServerCommandSource;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.Identifier;
import net.minecraft.world.tick.TickPriority;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;
import java.util.Map;

public class DoormatServer implements ModInitializer, CarpetExtension {

	public static final String MOD_ID = "doormat";
	public static final String MOD_NAME;
	public static final Version MOD_VERSION;

    public static final Logger LOGGER = LoggerFactory.getLogger(MOD_ID);
	public static int MAX_QC_RANGE = 1;

	public static List<AbstractTinkerKitCommand<?>> TINKER_KIT_COMMANDS = List.of(
		new QuasiConnectivityCommand(),
		new DelayCommand(),
		new UpdateTypeCommand(),
		new TickPriorityCommand()
	);

	static {
		ModMetadata metadata = FabricLoader.getInstance().getModContainer(MOD_ID).orElseThrow(RuntimeException::new).getMetadata();

		MOD_NAME = metadata.getName();
		MOD_VERSION = metadata.getVersion();
	}

	public static final boolean IS_DEBUG = DoormatSettings.incrediblySecretSetting || FabricLoader.getInstance().isDevelopmentEnvironment();

	@Override
	public void onInitialize() {
		CarpetServer.manageExtension(new DoormatServer());
        LOGGER.info("{} initialized. Wipe your feet!", MOD_NAME);

		// Handles all instances of most blocks for automatic inter-mod compatibility.
		// Modders should not need to add their own variants manually if the block in question extends any of these classes.
		TinkerKit.Registry.putBlocksByClass(DoorBlock.class, 0, 0, UpdateType.SHAPE, TickPriority.NORMAL);
		TinkerKit.Registry.putBlocksByClass(TrapdoorBlock.class, 0, 0, UpdateType.SHAPE, TickPriority.NORMAL);
		TinkerKit.Registry.putBlocksByClass(FenceGateBlock.class, 0, 0, UpdateType.SHAPE, TickPriority.NORMAL);
		TinkerKit.Registry.putBlocksByClass(AbstractSkullBlock.class, 0, 0, UpdateType.SHAPE, TickPriority.NORMAL);
		TinkerKit.Registry.putBlocksByClass(AbstractRedstoneGateBlock.class, 0, 2, UpdateType.SHAPE, null);
		TinkerKit.Registry.putBlocksByClass(DispenserBlock.class, 1, 4, UpdateType.SHAPE, TickPriority.NORMAL);
		TinkerKit.Registry.putBlocksByClass(RedstoneLampBlock.class, 0, 4, UpdateType.SHAPE, TickPriority.NORMAL);
		TinkerKit.Registry.putBlocksByClass(BulbBlock.class, 0, 0, UpdateType.BOTH, TickPriority.NORMAL);
		TinkerKit.Registry.putBlocksByClass(PistonBlock.class, 1, 0, null, TickPriority.NORMAL);
		TinkerKit.Registry.putBlocksByClass(TntBlock.class, 0, 0, UpdateType.BOTH, TickPriority.NORMAL);
		TinkerKit.Registry.putBlocksByClass(NoteBlock.class, 0, 0, UpdateType.BOTH, TickPriority.NORMAL);
		TinkerKit.Registry.putBlocksByClass(BellBlock.class, 0, 0, UpdateType.BOTH, TickPriority.NORMAL);
		TinkerKit.Registry.putBlocksByClass(HopperBlock.class, 0, 0, UpdateType.SHAPE, TickPriority.NORMAL);
		TinkerKit.Registry.putBlocksByClass(CommandBlock.class, 0, 1, null, TickPriority.NORMAL);
		TinkerKit.Registry.putBlocksByClass(StructureBlock.class, 0, 1, null, TickPriority.NORMAL);
		TinkerKit.Registry.putBlocksByClass(BarrelBlock.class, 0, 0, UpdateType.SHAPE, TickPriority.NORMAL);

		TinkerKit.Registry.putBlock(Blocks.RAIL, null, 0, UpdateType.BOTH, TickPriority.NORMAL);
		TinkerKit.Registry.putBlocks(0, 0, UpdateType.BOTH, TickPriority.NORMAL,
			Blocks.ACTIVATOR_RAIL,
			Blocks.POWERED_RAIL
		);
		TinkerKit.Registry.putBlock(Blocks.CRAFTER, 0, 4, UpdateType.SHAPE, TickPriority.NORMAL);

		TinkerKit.Registry.putBlock(Blocks.OBSERVER, null, 2, UpdateType.SHAPE, TickPriority.NORMAL);
		TinkerKit.Registry.putBlock(Blocks.REDSTONE_TORCH, null, 2, UpdateType.BOTH, TickPriority.NORMAL);
		TinkerKit.Registry.putBlock(Blocks.REDSTONE_WALL_TORCH, 0, 2, UpdateType.BOTH, TickPriority.NORMAL);
		TinkerKit.Registry.putBlock(Blocks.BIG_DRIPLEAF, 0, null, UpdateType.SHAPE, null);
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

		for (AbstractTinkerKitCommand<?> tinkerKitCommand : TINKER_KIT_COMMANDS)
			tinkerKitCommand.register(dispatcher, registryAccess);
	}

	@Override
	public void registerLoggers() {
		DoormatLoggers.register();
	}

	@Override
	public Map<String, String> canHasTranslations(String lang) {
		String path = String.format("assets/%s/lang/%s.json", MOD_ID, lang);
		return Translations.getTranslationFromResourcePath(path);
	}

	public static Identifier id(String name) {
		return Identifier.of(MOD_ID, name);
	}

}