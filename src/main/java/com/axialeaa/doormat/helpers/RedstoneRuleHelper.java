package com.axialeaa.doormat.helpers;

import carpet.CarpetSettings;
import carpet.helpers.QuasiConnectivity;
import com.axialeaa.doormat.util.RedstoneRule;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

import java.util.Map;

public class RedstoneRuleHelper {

    /**
     * @return true if the rule is enabled, carpet's quasiConnectivity is set to something above 0 and the block is powered directly or via QC, otherwise true if the block is directly powered.
     * @implNote This will also set the hashmap entry for the component to default if it doesn't already exist. It's weird to do it here, but it's a lot more robust against {@link NullPointerException}s than doing it in the {@link com.axialeaa.doormat.util.ConfigFile#load(MinecraftServer) ConfigFile.load()} method.
     */
    public static boolean quasiConnectForRule(World world, BlockPos pos, RedstoneRule component) {
        boolean isPowered = world.isReceivingRedstonePower(pos);
        if (!RedstoneRule.qcValues.containsKey(component))
            RedstoneRule.qcValues.put(component, component.getDefaultQCValue());
        return isPowered || (QuasiConnectivity.hasQuasiSignal(world, pos) && RedstoneRule.qcValues.get(component) && CarpetSettings.quasiConnectivity > 0);
    }

    /**
     * @return the update flag(s) for the given component.
     * @implNote This does the same hashmap thing as above, for update type instead of quasi-connectivity.
     */
    public static int getRuleFlags(RedstoneRule component) {
        if (!RedstoneRule.updateTypeValues.containsKey(component))
            RedstoneRule.updateTypeValues.put(component, component.getDefaultUpdateTypeValue());
        return RedstoneRule.updateTypeValues.get(component).ordinal();
    }

    /**
     * @return true if the rule flag is 1 or 3.
     * @implNote This is used for disabling certain instances of world.updateNeighbors(), and in principle this returns true for all odd numbers.
     */
    public static boolean shouldUpdateNeighbours(RedstoneRule component) {
        return (getRuleFlags(component) & 1) == 1;
    }

    /**
     * @param stringMap the hashmap, of which the strings will be converted to an array.
     * @return an array of strings, used for command autocompletion.
     */
    public static String[] getCommandSuggestions(Map<String, ?> stringMap) {
        return stringMap.keySet().toArray(new String[0]);
    }

}
