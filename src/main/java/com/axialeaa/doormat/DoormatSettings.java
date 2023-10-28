package com.axialeaa.doormat;

import carpet.CarpetSettings;
import carpet.api.settings.CarpetRule;
import carpet.api.settings.Rule;
import carpet.api.settings.Validator;
import carpet.api.settings.Validators;
import net.minecraft.server.command.ServerCommandSource;

import static carpet.api.settings.RuleCategory.*;

public class DoormatSettings {

    /*
     * -------- CATEGORIES --------
     */

    public static final String DOORMAT = "doormat";
    public static final String PARITY = "parity";
    public static final String UPDATE = "update";
    public static final String QC = "qc";

    /*
     * -------- VALIDATORS --------
     */

    private static class PistonMovementTimeValidator extends Validator<Integer> {
        @Override
        public Integer validate(ServerCommandSource source, CarpetRule<Integer> currentRule, Integer newValue, String typedString) {
            return (newValue >= 1 && newValue <= 1200) ? newValue : null;
        }
        @Override
        public String description() {
            return "You must choose a value from 1 to 1200";
        }
    }

    /*
     * -------- ENUMS --------
     */

    public enum PetHurtMode { FALSE, TRUE, OWNED; public boolean isEnabled() { return this != FALSE; } }
    public enum SporeBlossomsMode { FALSE, MOSS, SELF }

    @SuppressWarnings("unused")
    public enum NeighbourUpdateMode {

        NEITHER(0),
        BLOCK(1),
        SHAPE(2),
        BOTH(3);

        private final int flags;

        NeighbourUpdateMode(int flags) {
            this.flags = flags;
        }

        public int getFlags() {
            return flags;
        }

    }

    /*
     * -------- RULES --------
     */

    /* QUALITY OF LIFE */

    @Rule( categories = { FEATURE, DOORMAT } )
    public static boolean leavesNoCollision = false;

    @Rule( categories = { FEATURE, EXPERIMENTAL, DOORMAT } )
    public static boolean consistentItemExplosionDamage = false;

    @Rule( categories = { SURVIVAL, DOORMAT } )
    public static boolean playerDropAllXp = false;

    @Rule( categories = { SURVIVAL, DOORMAT } )
    public static boolean beaconsHealPets = false;

    @Rule( categories = { SURVIVAL, DOORMAT } )
    public static PetHurtMode disablePetAttacking = PetHurtMode.FALSE;

    @Rule( options = { "3", "7" }, validators = { Validators.NonNegativeNumber.class }, strict = false, categories = { SURVIVAL, DOORMAT } )
    public static int insomniaDaysSinceSlept = 3;

    @Rule( options = { "63", "127", "255" }, validators = { Validators.NonNegativeNumber.class }, strict = false, categories = { SURVIVAL, DOORMAT } )
    public static int phantomMinSpawnAltitude = 63;

    @Rule( categories = { CREATIVE, SURVIVAL, DOORMAT } )
    public static boolean disableShulkerReproduction = false;

    @Rule( options = { "8.0", "16.0" }, validators = { Validators.NonNegativeNumber.class }, strict = false, categories = { CREATIVE, DOORMAT } )
    public static double maxMinecartSpeed = 8.0;

    @Rule( categories = { CREATIVE, DOORMAT } )
    public static boolean disableNetherPortalCrossing = false;

    @Rule( categories = { CREATIVE, DOORMAT } )
    public static boolean disableEndPortalCrossing = false;

    /* FARMING */

    @Rule( categories = { FEATURE, DOORMAT } )
    public static boolean renewableGildedBlackstone = false;

    @Rule( categories = { FEATURE, DOORMAT } )
    public static boolean renewableCobwebs = false;

    @Rule( categories = { FEATURE, DOORMAT } )
    public static SporeBlossomsMode renewableSporeBlossoms = SporeBlossomsMode.FALSE;

    @Rule( categories = { FEATURE, DOORMAT } )
    public static boolean huskWashing = false; // technically renewable sand; renamed to avoid conflict with other carpet mods

    @Rule( categories = { FEATURE, DOORMAT } )
    public static boolean mossSpreadToCobblestone = false;

    @Rule( categories = { FEATURE, DOORMAT } )
    public static boolean mossSpreadToStoneBricks = false;

    @Rule( categories = { FEATURE, DOORMAT } )
    public static boolean forceGrassSpread = false;

    @Rule( categories = { FEATURE, DOORMAT } )
    public static boolean azaleaLeavesGrowFlowers = false;

    @Rule( categories = { FEATURE, DOORMAT } )
    public static boolean accurateAzaleaLeafDistribution = false;

    @Rule( categories = { FEATURE, DOORMAT } )
    public static boolean growableSwampOakTrees = false;

    @Rule( categories = { FEATURE, DOORMAT } )
    public static boolean propagulePropagation = false;

    @Rule( categories = { FEATURE, DOORMAT } )
    public static boolean zoglinsSpawnInPortals = false;

    /* FUN */

    @Rule( categories = { SURVIVAL, DOORMAT } )
    public static boolean thornyRoseBush = false;

    /* REDSTONE TINKERING */

    @Rule( categories = { FEATURE, DOORMAT } )
    public static CarpetSettings.ChainStoneMode stickyStickyPistons = CarpetSettings.ChainStoneMode.FALSE;

    @Rule( categories = { FEATURE, DOORMAT } )
    public static CarpetSettings.ChainStoneMode stickyPillarBlocks = CarpetSettings.ChainStoneMode.FALSE;

    @Rule( options = "2", validators = { PistonMovementTimeValidator.class }, strict = false, categories = { FEATURE, DOORMAT } )
    public static int pistonMovementTime = 2;

    @Rule( categories = { FEATURE, UPDATE, DOORMAT } )
    public static NeighbourUpdateMode lampUpdateType = NeighbourUpdateMode.SHAPE;

    @Rule( categories = { FEATURE, UPDATE, DOORMAT } )
    public static NeighbourUpdateMode doorUpdateType = NeighbourUpdateMode.SHAPE;

    @Rule( categories = { FEATURE, UPDATE, DOORMAT } )
    public static NeighbourUpdateMode trapdoorUpdateType = NeighbourUpdateMode.SHAPE;

    @Rule( categories = { FEATURE, UPDATE, DOORMAT } )
    public static NeighbourUpdateMode fenceGateUpdateType = NeighbourUpdateMode.SHAPE;

    @Rule( categories = { FEATURE, UPDATE, DOORMAT } )
    public static NeighbourUpdateMode pistonUpdateType = NeighbourUpdateMode.BOTH;

    @Rule( categories = { FEATURE, UPDATE, DOORMAT } )
    public static NeighbourUpdateMode noteBlockUpdateType = NeighbourUpdateMode.BOTH;

    @Rule( categories = { FEATURE, UPDATE, DOORMAT } )
    public static NeighbourUpdateMode hopperUpdateType = NeighbourUpdateMode.SHAPE;

    @Rule( categories = { FEATURE, UPDATE, DOORMAT } )
    public static NeighbourUpdateMode dispenserUpdateType = NeighbourUpdateMode.SHAPE;

    @Rule( categories = { FEATURE, UPDATE, DOORMAT } )
    public static NeighbourUpdateMode railUpdateType = NeighbourUpdateMode.BOTH;

    @Rule( categories = { FEATURE, UPDATE, DOORMAT } )
    public static NeighbourUpdateMode bellUpdateType = NeighbourUpdateMode.BOTH;

    @Rule( categories = { FEATURE, UPDATE, DOORMAT } )
    public static NeighbourUpdateMode tntUpdateType = NeighbourUpdateMode.BOTH;

    @Rule( categories = { FEATURE, QC, DOORMAT } )
    public static boolean lampQuasiConnecting = false;

    @Rule( categories = { FEATURE, QC, DOORMAT } )
    public static boolean trapdoorQuasiConnecting = false;

    @Rule( categories = { FEATURE, QC, DOORMAT } )
    public static boolean fenceGateQuasiConnecting = false;

    @Rule( categories = { FEATURE, QC, DOORMAT } )
    public static boolean noteBlockQuasiConnecting = false;

    @Rule( categories = { FEATURE, QC, DOORMAT } )
    public static boolean hopperQuasiConnecting = false;

    @Rule( categories = { FEATURE, QC, DOORMAT } )
    public static boolean railQuasiConnecting = false;

    @Rule( categories = { FEATURE, QC, DOORMAT } )
    public static boolean bellQuasiConnecting = false;

    @Rule( categories = { FEATURE, QC, DOORMAT } )
    public static boolean tntQuasiConnecting = false;

    /* PARITY */

    @Rule( categories = { FEATURE, PARITY, DOORMAT } )
    public static boolean parityPortalTicking = false;

    @Rule( categories = { FEATURE, PARITY, DOORMAT } )
    public static boolean parityFireAspectLighting = false;

    @Rule( categories = { FEATURE, PARITY, DOORMAT } )
    public static boolean parityRavagersStompPlants = false;

    @Rule( categories = { FEATURE, PARITY, DOORMAT } )
    public static boolean parityComparatorsReadThroughChains = false;

    @Rule( categories = { FEATURE, PARITY, DOORMAT } )
    public static boolean parityComparatorsReadThroughPaths = false;

    /* COMMANDS */

    @Rule( categories = { COMMAND, DOORMAT } )
    public static String commandRandomTick = "true";

}