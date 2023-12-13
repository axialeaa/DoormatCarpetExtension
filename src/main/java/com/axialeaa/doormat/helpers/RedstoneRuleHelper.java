package com.axialeaa.doormat.helpers;

import carpet.CarpetSettings;
import carpet.helpers.QuasiConnectivity;
import com.axialeaa.doormat.util.QuasiConnectivityRules;
import com.axialeaa.doormat.util.UpdateTypeRules;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

import java.util.Map;

public class RedstoneRuleHelper {

    /**
     * @return true if the rule is enabled, carpet's quasiConnectivity is set to something above 0 and the block is powered directly or via QC, otherwise true if the block is directly powered.
     */
    public static boolean quasiConnectForRule(World world, BlockPos pos, QuasiConnectivityRules component) {
        boolean isPowered = world.isReceivingRedstonePower(pos);
        return isPowered || (QuasiConnectivity.hasQuasiSignal(world, pos) && QuasiConnectivityRules.ruleValues.get(component) && CarpetSettings.quasiConnectivity > 0);
    }

    /**
     * @return true if the rule flag is 1 or 3.
     * @implNote This is used for disabling certain instances of world.updateNeighbors(), and in principle this returns true for all odd numbers. The only odd numbers present in the flags of the UpdateMode enum entries are BLOCK(1) and BOTH(3).
     */
    public static boolean neighbourUpdateForRule(UpdateTypeRules rule) {
        return (UpdateTypeRules.ruleValues.get(rule).getFlags() & 1) == 1;
    }

    /**
     * @param stringMap the hashmap, of which the strings will be converted to an array
     * @return an array of strings, used for command autocompletion.
     */
    public static String[] getCommandSuggestions(Map<String, ?> stringMap) {
        return stringMap.keySet().toArray(new String[0]);
    }

}
