package com.axialeaa.doormat;

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
    public static final String PISTON = "piston";
    public static final String FUN = "fun";

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

    public enum PetHurtMode { FALSE, TRUE, IF_OWNER; public boolean isEnabled() {
            return this != FALSE;
        } }
    public enum RenewableSporeBlossomsMode { FALSE, MOSS, SELF }
    public enum StickyBlockMode { FALSE, TRUE, STICK_TO_ALL; public boolean isEnabled() {
            return this != FALSE;
        } }
    public enum NeighbourUpdateMode {

        NONE(0),
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

    @Rule( categories = { FEATURE, DOORMAT } )
    public static boolean consistentItemExplosionDamage = false;

    @Rule( categories = { SURVIVAL, DOORMAT } )
    public static boolean beaconsHealPets = false;

    @Rule( categories = { SURVIVAL, DOORMAT } )
    public static PetHurtMode disablePetAttacking = PetHurtMode.FALSE;

    @Rule( options = "3", validators = { Validators.NonNegativeNumber.class }, strict = false, categories = { SURVIVAL, DOORMAT } )
    public static int insomniaDaysSinceSlept = 3;

    /* FARMING */

    @Rule( categories = { FEATURE, DOORMAT } )
    public static boolean renewableGildedBlackstone = false;

    @Rule( categories = { FEATURE, DOORMAT } )
    public static boolean renewableCobwebs = false;

    @Rule( categories = { FEATURE, DOORMAT } )
    public static RenewableSporeBlossomsMode renewableSporeBlossoms = RenewableSporeBlossomsMode.FALSE;

    @Rule( categories = { FEATURE, DOORMAT } )
    public static boolean huskWashing = false; // technically renewable sand, renamed to avoid conflict with other carpet mods

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
    public static boolean growSwampOakTrees = false;

    /* FUN */

    @Rule( categories = { SURVIVAL, DOORMAT } )
    public static boolean thornyRoseBush = false;

    /* REDSTONE FUN */

    @Rule( categories = { FEATURE, DOORMAT, PISTON, FUN } )
    public static StickyBlockMode stickyStickyPistons = StickyBlockMode.FALSE;

    @Rule( categories = { FEATURE, DOORMAT, PISTON, FUN } )
    public static StickyBlockMode stickyPillarBlocks = StickyBlockMode.FALSE;

    @Rule( options = "2", validators = { PistonMovementTimeValidator.class }, strict = false, categories = { FEATURE, DOORMAT, PISTON, FUN } )
    public static int pistonMovementTime = 2;

    @Rule( categories = { FEATURE, DOORMAT, FUN } )
    public static NeighbourUpdateMode lampUpdateType = NeighbourUpdateMode.SHAPE;

    @Rule( categories = { FEATURE, DOORMAT, FUN } )
    public static NeighbourUpdateMode doorUpdateType = NeighbourUpdateMode.SHAPE;

    @Rule( categories = { FEATURE, DOORMAT, FUN } )
    public static NeighbourUpdateMode noteBlockUpdateType = NeighbourUpdateMode.BOTH;

    @Rule( categories = { FEATURE, DOORMAT, FUN } )
    public static NeighbourUpdateMode hopperUpdateType = NeighbourUpdateMode.SHAPE;

    @Rule( categories = { FEATURE, DOORMAT, FUN } )
    public static NeighbourUpdateMode dispenserUpdateType = NeighbourUpdateMode.SHAPE;

    @Rule( categories = { FEATURE, DOORMAT, FUN } )
    public static NeighbourUpdateMode bellUpdateType = NeighbourUpdateMode.BOTH;

    /* PARITY */

    @Rule( categories = { FEATURE, DOORMAT, PARITY } )
    public static boolean parityPortalTicking = false;

    /* COMMANDS */

    @Rule( categories = { COMMAND, DOORMAT } )
    public static String commandIdentity = "true";

}