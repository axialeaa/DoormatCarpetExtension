package com.axialeaa.doormat;

import carpet.CarpetSettings;
import carpet.api.settings.CarpetRule;
import carpet.api.settings.Rule;
import carpet.api.settings.Validator;
import carpet.api.settings.Validators;
import net.minecraft.server.command.ServerCommandSource;

import static carpet.api.settings.RuleCategory.*;

public class DoormatSettings {

    /**<h1>CATEGORIES</h1>*/

    public static final String DOORMAT = "doormat";
    public static final String PARITY = "parity";
    public static final String TOOLTIP = "tooltip";
    public static final String APRIL_FOOLS = "april_fools";
    public static final String RETRO = "retro";
    public static final String TINKERING = "tinkering";

    /**<h1>VALIDATORS</h1>*/

    private static class TimeFromOneValidator extends Validator<Integer> {

        @Override
        public Integer validate(ServerCommandSource source, CarpetRule<Integer> currentRule, Integer newValue, String typedString) {
            return (newValue >= 1 && newValue <= 1200) ? newValue : null;
        }

        @Override
        public String description() {
            return "You must choose a value from 1 to 1200";
        }

    }

    private static class TimeFromZeroValidator extends Validator<Integer> {

        @Override
        public Integer validate(ServerCommandSource source, CarpetRule<Integer> currentRule, Integer newValue, String typedString) {
            return (newValue >= 0 && newValue <= 1200) ? newValue : null;
        }

        @Override
        public String description() {
            return "You must choose a value from 0 to 1200";
        }

    }

    private static class WorldHeightValidator extends Validator<Integer> {

        @Override
        public Integer validate(ServerCommandSource source, CarpetRule<Integer> currentRule, Integer newValue, String typedString) {
            return (newValue >= -63 && newValue <= 320) ? newValue : null;
        }

        @Override
        public String description() {
            return "You must choose a value from -63 to 320";
        }

    }

    /**<h1>ENUMS</h1>*/

    public enum PetAttackMode {
        FALSE, TRUE, OWNED;
        public boolean enabled() {
            return this != FALSE;
        }
    }

    public enum PeacefulMonstersMode {
        FALSE, TRUE, BELOW_SURFACE, BELOW_SEA, UNNATURAL;
        public boolean enabled() {
            return this != FALSE;
        }
    }

    public enum PotTooltipMode {
        FALSE, TRUE, IGNORE_BRICKS;
        public boolean enabled() {
            return this != FALSE;
        }
    }

    public enum TrimTooltipMode {
        FALSE, TRUE, ONLY_PATTERN;
        public boolean enabled() {
            return this != FALSE;
        }
    }

    public enum ChiseledBookshelfSignalMode {
        INTERACTION, FULLNESS, FULLNESS_LERPED
    }

    /**<h1>RULES</h1>*/

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

    @Rule( categories = { TINKERING, DOORMAT } )
    public static ChiseledBookshelfSignalMode chiseledBookshelfSignalBasis = ChiseledBookshelfSignalMode.INTERACTION;

    @Rule( categories = { CLIENT, TOOLTIP, DOORMAT } )
    public static boolean compactEnchantTooltips = false;

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

    @Rule( options = { "0", "1" }, validators = TimeFromZeroValidator.class, strict = false, categories = { TINKERING, DOORMAT } )
    public static int copperBulbDelay = 0;

    @Rule( categories = { TINKERING, DOORMAT } )
    public static boolean copperBulbOxidationSignal = false;

    @Rule( categories = { FEATURE, DOORMAT } )
    public static boolean cryingObsidianPortalFrames = false;

    @Rule( categories = { FEATURE, DOORMAT } )
    public static boolean deepslateDungeons = false;

    @Rule( categories = { CREATIVE, DOORMAT } )
    public static boolean disableEndPortalCrossing = false;

    @Rule( categories = { CREATIVE, DOORMAT } )
    public static boolean disableNetherPortalCrossing = false;

    @Rule( categories = { SURVIVAL, DOORMAT } )
    public static PetAttackMode disablePetAttacking = PetAttackMode.FALSE;

    @Rule( categories = { CREATIVE, SURVIVAL, DOORMAT } )
    public static boolean disableShulkerReproduction = false;

    @Rule( options = { "0.0", "0.075" }, validators = Validators.Probablity.class, strict = false, categories = { FEATURE, DOORMAT } )
    public static double fastLeafDecay = 0.0;

    @Rule( categories = { PARITY, DOORMAT } )
    public static boolean fireAspectLighting = false;

    @Rule( categories = { FEATURE, DOORMAT } )
    public static boolean forceGrassSpread = false;

    @Rule( categories = { FEATURE, DOORMAT } )
    public static boolean growableSwampOakTrees = false;

    @Rule( options = "8", validators = TimeFromOneValidator.class, strict = false, categories = { TINKERING, APRIL_FOOLS, DOORMAT } )
    public static int hopperTransferTime = 8;

    @Rule( categories = { FEATURE, DOORMAT } )
    public static boolean huskWashing = false; // technically renewable sand; renamed to avoid conflict with other carpet mods

    @Rule( categories = { EXPERIMENTAL, DOORMAT } )
    public static boolean incrediblySecretSetting = false;

    @Rule( options = { "3", "7" }, validators = Validators.NonNegativeNumber.class, strict = false, categories = { SURVIVAL, DOORMAT } )
    public static int insomniaDayCount = 3;

    @Rule( categories = { TINKERING, DOORMAT } )
    public static boolean jukeboxDiscProgressSignal = false;

    @Rule( categories = { CREATIVE, DOORMAT } )
    public static boolean leavesNoCollision = false;

    @Rule( options = { "8.0", "16.0" }, validators = Validators.NonNegativeNumber.class, strict = false, categories = { TINKERING, DOORMAT } )
    public static double maxMinecartSpeed = 8.0;

    @Rule( categories = { FEATURE, DOORMAT } )
    public static boolean mossSpreadToCobblestone = false;

    @Rule( categories = { FEATURE, DOORMAT } )
    public static boolean mossSpreadToStoneBricks = false;

    @Rule( categories = { SURVIVAL, EXPERIMENTAL, DOORMAT } )
    public static PeacefulMonstersMode peacefulMonsterSpawning = PeacefulMonstersMode.FALSE;

    @Rule( options = { "63", "127", "255" }, validators = WorldHeightValidator.class, strict = false, categories = { SURVIVAL, DOORMAT } )
    public static int phantomMinSpawnAltitude = 63;

    @Rule( options = "2", validators = TimeFromOneValidator.class, strict = false, categories = { TINKERING, DOORMAT } )
    public static int pistonMovementTime = 2;

    @Rule( categories = { SURVIVAL, DOORMAT } )
    public static boolean playersDropAllXp = false;

    @Rule( categories = { PARITY, DOORMAT } )
    public static boolean portalForceTicking = false;

    @Rule( categories = { FEATURE, DOORMAT } )
    public static boolean propagulePropagation = false;

    @Rule( categories = { PARITY, DOORMAT } )
    public static boolean ravagersStompPlants = false;

    @Rule( categories = { CLIENT, PARITY, DOORMAT } )
    public static boolean reachAroundBridging = false;

    @Rule( categories = { TINKERING, DOORMAT } )
    public static boolean redstoneOpensBarrels = false;

    @Rule( categories = { FEATURE, DOORMAT } )
    public static boolean renewableCobwebs = false;

    @Rule( categories = { FEATURE, DOORMAT } )
    public static boolean renewableGildedBlackstone = false;

    @Rule( categories = { FEATURE, DOORMAT } )
    public static boolean renewableSporeBlossoms = false;

    @Rule( categories = { RETRO, DOORMAT } )
    public static boolean retroRepeaterDelay = false;

    @Rule( categories = { PARITY, DOORMAT } )
    public static boolean softInversion = false;

    @Rule( categories = { APRIL_FOOLS, DOORMAT } )
    public static boolean solidEntityCollision = false;

    @Rule( categories = { FEATURE, DOORMAT } )
    public static boolean sporeBlossomDuplication = false;

    @Rule( categories = { FEATURE, DOORMAT } )
    public static CarpetSettings.ChainStoneMode stickyPillarBlocks = CarpetSettings.ChainStoneMode.FALSE;

    @Rule( categories = { FEATURE, APRIL_FOOLS, DOORMAT } )
    public static CarpetSettings.ChainStoneMode stickyStickyPistons = CarpetSettings.ChainStoneMode.FALSE;

    @Rule( categories = { SURVIVAL, DOORMAT } )
    public static boolean thornyRoseBush = false;

    @Rule( options = "8", validators = Validators.NonNegativeNumber.class, strict = false, categories = { TINKERING, DOORMAT } )
    public static int torchBurnoutFlickerAmount = 8;

    @Rule( options = { "60", "40" }, validators = TimeFromOneValidator.class, strict = false, categories = { TINKERING, DOORMAT } )
    public static int torchBurnoutTime = 60;

    @Rule( categories = { CREATIVE, DOORMAT } )
    public static boolean trialSpawnerSheepActivation = false;

    @Rule( categories = { FEATURE, DOORMAT } )
    public static boolean zoglinsSpawnInPortals = false;

    /**<h2>COMMANDS</h2>*/

    @Rule( categories = { COMMAND, TINKERING, DOORMAT } )
    public static String commandQC = "true";

    @Rule( categories = { COMMAND, TINKERING, DOORMAT } )
    public static String commandUpdateType = "true";

    @Rule( categories = { COMMAND, DOORMAT } )
    public static String commandRandomTick = "true";

}