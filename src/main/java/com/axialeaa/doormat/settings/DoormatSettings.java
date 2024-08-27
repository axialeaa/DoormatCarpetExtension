package com.axialeaa.doormat.settings;

import carpet.CarpetSettings;
import carpet.api.settings.Rule;
import carpet.api.settings.Validators;
import com.axialeaa.doormat.settings.enum_options.*;
import com.axialeaa.doormat.settings.enum_options.function.*;
import com.axialeaa.doormat.settings.validators.*;

import static carpet.api.settings.RuleCategory.*;

public class DoormatSettings {

    public static final String DOORMAT = "doormat";
    public static final String PARITY = "parity";
    public static final String TOOLTIP = "tooltip";
    public static final String APRIL_FOOLS = "april_fools";
    public static final String RETRO = "retro";
    public static final String TINKERING = "tinkering";

    @Rule( categories = { FEATURE, DOORMAT } )
    public static boolean accurateAzaleaLeafDistribution = false;

    @Rule( categories = { CREATIVE, DOORMAT } )
    public static boolean alwaysHalloween = false;

    @Rule( categories = { FEATURE, DOORMAT } )
    public static boolean azaleaLeavesGrowFlowers = false;

    @Rule( categories = { SURVIVAL, DOORMAT } )
    public static boolean beaconsHealPets = false;

    @Rule( categories = { TINKERING, DOORMAT } )
    public static boolean barrelItemDumping = false;

    @Rule( options = "minecraft:basalt", validators = BlockIdentifierValidator.class, strict = false, categories = { TINKERING, APRIL_FOOLS, EXPERIMENTAL, DOORMAT } )
    public static String basaltGenProduct = "minecraft:basalt";

    @Rule( options = "2", validators = TimeFromZeroValidator.class, strict = false, categories = { TINKERING, DOORMAT } )
    public static int blockFallingDelay = 2;

    @Rule( categories = { FEATURE, DOORMAT } )
    public static boolean biomeDependentDungeonMobs = false;

    @Rule( categories = { SURVIVAL, DOORMAT } )
    public static boolean campfireRespawning = false;

    @Rule( categories = { TINKERING, DOORMAT } )
    public static ChiseledBookshelfSignalMode chiseledBookshelfFullnessSignal = ChiseledBookshelfSignalMode.FALSE;

    @Rule( options = "minecraft:cobblestone", validators = BlockIdentifierValidator.class, strict = false, categories = { TINKERING, APRIL_FOOLS, EXPERIMENTAL, DOORMAT } )
    public static String cobblestoneGenProduct = "minecraft:cobblestone";

    @Rule( categories = { CLIENT, TOOLTIP, DOORMAT } )
    public static CarouselTooltipMode compactBannerTooltips = CarouselTooltipMode.FALSE;

    @Rule( categories = { CLIENT, TOOLTIP, DOORMAT } )
    public static CarouselTooltipMode compactEnchantTooltips = CarouselTooltipMode.FALSE;

    @Rule( categories = { CLIENT, TOOLTIP, DOORMAT } )
    public static PotTooltipMode compactPotTooltips = PotTooltipMode.FALSE;

    @Rule( categories = { CLIENT, TOOLTIP, DOORMAT } )
    public static boolean compactTemplateTooltips = false;

    @Rule( categories = { CLIENT, TOOLTIP, DOORMAT } )
    public static TrimTooltipMode compactTrimTooltips = TrimTooltipMode.FALSE;

    @Rule( categories = { PARITY, DOORMAT } )
    public static boolean comparatorsReadThroughChains = false;

    @Rule( categories = { PARITY, DOORMAT } )
    public static boolean comparatorsReadThroughPaths = false;

    @Rule( categories = { PARITY, DOORMAT } )
    public static boolean comparatorsReadThroughPistons = false;

    @Rule( categories = { FEATURE, DOORMAT } )
    public static boolean composterSideInputs = false;

    @Rule( categories = { EXPERIMENTAL, BUGFIX, DOORMAT } )
    public static boolean consistentExplosionImmunity = false;

    @Rule( categories = { BUGFIX, DOORMAT } )
    public static boolean consistentWaterlogPushing = false;

    @Rule( categories = { TINKERING, DOORMAT } )
    public static boolean copperBulbOxidationSignal = false;

    @Rule( categories = { TINKERING, DOORMAT } )
    public static boolean crafterSignalLerping = false;

    @Rule( categories = { FEATURE, DOORMAT } )
    public static boolean cryingObsidianPortalFrames = false;

    @Rule( categories = { FEATURE, DOORMAT } )
    public static boolean deepslateDungeons = false;

    @Rule( categories = { CREATIVE, SURVIVAL, DOORMAT } )
    public static boolean disableBedTooFarWarning = false;

    @Rule( categories = { CREATIVE, SURVIVAL, DOORMAT } )
    public static boolean disableBedUnsafeWarning = false;

    @Rule( categories = { CREATIVE, DOORMAT } )
    public static boolean disableDragonEggTeleportation = false;

    @Rule( categories = { CREATIVE, DOORMAT } )
    public static boolean disableEndGatewayCrossing = false;

    @Rule( categories = { CREATIVE, DOORMAT } )
    public static boolean disableEndPortalCrossing = false;

    @Rule( categories = { CREATIVE, DOORMAT } )
    public static boolean disableMaceKnockback = false;

    @Rule( categories = { CREATIVE, DOORMAT } )
    public static boolean disableNetherPortalCrossing = false;

    @Rule( categories = { SURVIVAL, DOORMAT } )
    public static PetAttackMode disablePetAttacking = PetAttackMode.FALSE;

    @Rule( categories = { BUGFIX, OPTIMIZATION, DOORMAT } )
    public static boolean disablePrematureBlockFalling = false;

    @Rule( categories = { CREATIVE, SURVIVAL, DOORMAT } )
    public static boolean disableShulkerReproduction = false;

    @Rule( options = "5", validators = TimeFromZeroValidator.class, strict = false, categories = { TINKERING, DOORMAT } )
    public static int dragonEggFallingDelay = 5;

    @Rule( categories = { DISPENSER, DOORMAT } )
    public static boolean dispensersBuryItems = false;

    @Rule( categories = { BUGFIX, DOORMAT } )
    public static boolean endExitIgnoreLeaves = true;

    @Rule( categories = { RETRO, DOORMAT } )
    public static SillyStringMode endPlatformSillyString = SillyStringMode.FALSE;

    @Rule( options = { "0.0", "0.075" }, validators = Validators.Probablity.class, strict = false, categories = { FEATURE, DOORMAT } )
    public static double fastLeafDecayingChance = 0.0;

    @Rule( categories = { PARITY, DOORMAT } )
    public static boolean fireAspectLighting = false;

    @Rule( categories = { FEATURE, DOORMAT } )
    public static boolean forceGrassSpreading = false;

    @Rule( options = { "0.0", "0.002", "1.0" }, validators = Validators.Probablity.class, strict = false, categories = { FEATURE, DOORMAT } )
    public static double grassAgingChance = 0.0;

    @Rule( categories = { FEATURE, DOORMAT } )
    public static boolean growableSwampOakTrees = false;

    @Rule( categories = { FEATURE, DOORMAT } )
    public static boolean highlightSleepDeprivers = false;

    @Rule( options = "8", validators = TimeFromOneValidator.class, strict = false, categories = { TINKERING, APRIL_FOOLS, DOORMAT } )
    public static int hopperTransferTime = 8;

    @Rule( categories = { FEATURE, DOORMAT } )
    public static boolean huskWashing = false; // technically renewable sand; renamed to avoid conflict with other carpet mods

    @Rule( categories = { EXPERIMENTAL, DOORMAT } )
    public static boolean incrediblySecretSetting = false;

    @Rule( options = { "3", "7" }, validators = Validators.NonNegativeNumber.class, strict = false, categories = { SURVIVAL, DOORMAT } )
    public static int insomniaDayCount = 3;

    @Rule( options = { "0.0" }, validators = Validators.NonNegativeNumber.class, strict = false, categories = { SURVIVAL, DOORMAT } )
    public static double itemMagnetRange = 0.0;

    @Rule( options = { "0.2", "0.5", "1.0" }, validators = Validators.NonNegativeNumber.class, strict = false, categories = { SURVIVAL, DOORMAT } )
    public static double itemMagnetVelocity = 0.2;

    @Rule( categories = { TINKERING, DOORMAT } )
    public static boolean jukeboxDiscProgressSignal = false;

    @Rule( options = "30", validators = TimeFromOneValidator.class, strict = false, categories = { TINKERING, DOORMAT } )
    public static int lavaFlowSpeedDefault = 30;

    @Rule( options = "10", validators = TimeFromOneValidator.class, strict = false, categories = { TINKERING, DOORMAT } )
    public static int lavaFlowSpeedNether = 10;

    @Rule( categories = { PARITY, DOORMAT } )
    public static boolean lazyLoadedShearSuppression = false;

    @Rule( categories = { CREATIVE, DOORMAT } )
    public static boolean leavesNoCollision = false;

    @Rule( options = { "8.0", "16.0" }, validators = Validators.NonNegativeNumber.class, strict = false, categories = { TINKERING, DOORMAT } )
    public static double maxMinecartSpeedLand = 8.0;

    @Rule( options = { "4.0", "6.0" }, validators = Validators.NonNegativeNumber.class, strict = false, categories = { TINKERING, DOORMAT } )
    public static double maxMinecartSpeedWater = 4.0;

    @Rule( options = "3", validators = OverworldHeightValidator.class, strict = false, categories = { TINKERING, DOORMAT } )
    public static int maxSugarcaneGrowthHeight = 3;

    @SuppressWarnings("unused")
    @Rule( categories = { CREATIVE, EXPERIMENTAL, DOORMAT } )
    public static boolean moreTimeArgumentUnits = false;

    @Rule( categories = { FEATURE, DOORMAT } )
    public static boolean movableEnchantingTables = false;

    @Rule( categories = { FEATURE, DOORMAT } )
    public static boolean movableEnderChests = false;

    @Rule( categories = { FEATURE, DOORMAT } )
    public static boolean mossSpreadToCobblestone = false;

    @Rule( categories = { FEATURE, DOORMAT } )
    public static boolean mossSpreadToStoneBricks = false;

    @Rule( categories = { CREATIVE, DOORMAT } )
    public static boolean observerHalfDelay = false;

    @Rule( categories = { FEATURE, DOORMAT } )
    public static boolean openDoubleDoors = false;

    @Rule( options = { "0.0", "0.01", "1.0" }, validators = Validators.Probablity.class, strict = false, categories = { FEATURE, DOORMAT } )
    public static double obsidianFrameConversionChance = 0.0;

    @Rule( options = "minecraft:obsidian", validators = BlockIdentifierValidator.class, strict = false, categories = { TINKERING, APRIL_FOOLS, EXPERIMENTAL, DOORMAT } )
    public static String obsidianGenProduct = "minecraft:obsidian";

    @Rule( categories = { SURVIVAL, EXPERIMENTAL, DOORMAT } )
    public static PeacefulMonstersMode peacefulMonsterSpawning = PeacefulMonstersMode.FALSE;

    @Rule( categories = { CREATIVE, DOORMAT } )
    public static boolean persistentRespawnAnchorCharge = false;

    @Rule( options = { "63", "127", "255" }, validators = OverworldHeightValidator.class, strict = false, categories = { SURVIVAL, DOORMAT } )
    public static int phantomMinSpawnAltitude = 63;

    @Rule( categories = { CREATIVE, DOORMAT } )
    public static PiglinBlockGuardingMode piglinBlockGuarding = PiglinBlockGuardingMode.INTERACTED;

    @Rule( options = "2", validators = TimeFromOneValidator.class, strict = false, categories = { TINKERING, DOORMAT } )
    public static int pistonMovementTime = 2;

    @Rule( categories = { SURVIVAL, DOORMAT } )
    public static boolean playersDropAllXp = false;

    @Rule( categories = { PARITY, DOORMAT } )
    public static boolean portalForceTicking = false;

    @Rule( categories = { PARITY, DOORMAT } )
    public static boolean powderSnowPortalBreaking = false;

    @Rule( categories = { FEATURE, DOORMAT } )
    public static boolean propagulePropagation = false;

    @Rule( categories = { CREATIVE, DOORMAT } )
    public static boolean qcSuppressor = false;

    @Rule( categories = { PARITY, DOORMAT } )
    public static boolean ravagersStompPlants = false;

    @Rule( categories = { PARITY, DOORMAT } )
    public static boolean ravagersStompSnow = false;

    @Rule( categories = { CLIENT, PARITY, DOORMAT } )
    public static boolean reachAroundBridging = false;

    @Rule( categories = { TINKERING, DOORMAT } )
    public static boolean redstoneOpensBarrels = false;

    @Rule( categories = { FEATURE, DOORMAT } )
    public static boolean spawnersGenerateCobwebs = false;

    @Rule( options = { "0.0", "0.001", "1.0" }, validators = Validators.Probablity.class, strict = false, categories = { FEATURE, DOORMAT } )
    public static double renewableGildedBlackstone = 0.0;

    @Rule( categories = { FEATURE, DOORMAT } )
    public static boolean renewableSporeBlossoms = false;

    @Rule( categories = { RETRO, DOORMAT } )
    public static boolean retroRepeaterDelay = false;

    @Rule( categories = { SURVIVAL, DOORMAT } )
    public static boolean safeSand = false;

    @Rule( options = { "5", "20" }, validators = Validators.NonNegativeNumber.class, strict = false, categories = { RETRO, DOORMAT } )
    public static int sculkCatalystXpCount = 5;

    @Rule( options = { "0.0", "0.5", "1.0" }, validators = Validators.Probablity.class, strict = false, categories = { SURVIVAL, DOORMAT } )
    public static double sneakingRandomTickChance = 0.0;

    @Rule( categories = { PARITY, DOORMAT } )
    public static boolean softInversion = false;

    @Rule( categories = { FEATURE, DOORMAT } )
    public static boolean snowFormsUnderLeaves = false;

    @Rule( categories = { APRIL_FOOLS, DOORMAT } )
    public static boolean solidEntityCollision = false;

    @Rule( categories = { FEATURE, DOORMAT } )
    public static boolean sporeBlossomDuplication = false;

    @Rule( categories = { FEATURE, DOORMAT } )
    public static CarpetSettings.ChainStoneMode stickyPillarBlocks = CarpetSettings.ChainStoneMode.FALSE;

    @Rule( categories = { FEATURE, APRIL_FOOLS, DOORMAT } )
    public static CarpetSettings.ChainStoneMode stickyStickyPistons = CarpetSettings.ChainStoneMode.FALSE;

    @Rule( options = "minecraft:stone", validators = BlockIdentifierValidator.class, strict = false, categories = { TINKERING, APRIL_FOOLS, EXPERIMENTAL, DOORMAT } )
    public static String stoneGenProduct = "minecraft:stone";

    @Rule( categories = { BUGFIX, DOORMAT } )
    public static boolean suspiciousLootAmountFix = false;

    @Rule( categories = { BUGFIX, DOORMAT } )
    public static boolean suspiciousLootDropPosFix = false;

    @Rule( options = "8", validators = Validators.NonNegativeNumber.class, strict = false, categories = { TINKERING, DOORMAT } )
    public static int torchBurnoutFlickerAmount = 8;

    @Rule( options = { "60", "40" }, validators = TimeFromOneValidator.class, strict = false, categories = { TINKERING, DOORMAT } )
    public static int torchBurnoutTime = 60;

    @Rule( categories = { CREATIVE, DOORMAT } )
    public static EntityDetectorMode trialSpawnerEntityRequirement = EntityDetectorMode.SURVIVAL_PLAYERS;

    @Rule( categories = { SURVIVAL, DOORMAT } )
    public static boolean villagersAlwaysInheritBiome = false;

    @Rule( options = "5", validators = TimeFromOneValidator.class, strict = false, categories = { TINKERING, DOORMAT } )
    public static int waterFlowSpeed = 5;

    @Rule( categories = { RETRO, DOORMAT } )
    public static boolean woolDesertPyramids = false;

    @Rule( options = { "0.0", "0.1", "1.0" }, validators = Validators.Probablity.class, strict = false, categories = { FEATURE, DOORMAT } )
    public static double zoglinPortalSpawnChance = 0.0;

    @Rule( categories = { COMMAND, TINKERING, DOORMAT } )
    public static String commandQC = "true";

    @Rule( categories = { COMMAND, TINKERING, DOORMAT } )
    public static String commandDelay = "true";

    @Rule( categories = { COMMAND, TINKERING, DOORMAT } )
    public static String commandUpdateType = "true";

    @Rule( categories = { COMMAND, TINKERING, DOORMAT } )
    public static String commandTickPriority = "true";

    @Rule( categories = { COMMAND, DOORMAT } )
    public static String commandRandomTick = "true";

}