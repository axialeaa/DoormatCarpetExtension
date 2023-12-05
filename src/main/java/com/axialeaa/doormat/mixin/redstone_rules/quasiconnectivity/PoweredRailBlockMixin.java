package com.axialeaa.doormat.mixin.redstone_rules.quasiconnectivity;

import com.axialeaa.doormat.helpers.RedstoneRuleHelper;
import com.axialeaa.doormat.util.QuasiConnectivityRules;
import net.minecraft.block.PoweredRailBlock;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

@Mixin(PoweredRailBlock.class)
public class PoweredRailBlockMixin {

    @Redirect(method = "isPoweredByOtherRails(Lnet/minecraft/world/World;Lnet/minecraft/util/math/BlockPos;ZILnet/minecraft/block/enums/RailShape;)Z", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/World;isReceivingRedstonePower(Lnet/minecraft/util/math/BlockPos;)Z"))
    private boolean allowQuasiConnecting_isPoweredByOtherRails(World world, BlockPos pos) {
        return RedstoneRuleHelper.quasiConnectForRule(world, pos, QuasiConnectivityRules.RAIL);
    }

    @Redirect(method = "updateBlockState", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/World;isReceivingRedstonePower(Lnet/minecraft/util/math/BlockPos;)Z"))
    private boolean allowQuasiConnecting_updateBlockState(World world, BlockPos pos) {
        return RedstoneRuleHelper.quasiConnectForRule(world, pos, QuasiConnectivityRules.RAIL);
    }

}
