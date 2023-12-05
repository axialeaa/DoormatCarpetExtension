package com.axialeaa.doormat.mixin.redstone_rules.quasiconnectivity;

import com.axialeaa.doormat.helpers.RedstoneRuleHelper;
import com.axialeaa.doormat.util.QuasiConnectivityRules;
import net.minecraft.block.BulbBlock;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.math.BlockPos;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

@Mixin(BulbBlock.class)
public class BulbBlockMixin {

    @Redirect(method = "update", at = @At(value = "INVOKE", target = "Lnet/minecraft/server/world/ServerWorld;isReceivingRedstonePower(Lnet/minecraft/util/math/BlockPos;)Z"))
    private boolean allowQuasiConnecting(ServerWorld world, BlockPos pos) {
        return RedstoneRuleHelper.quasiConnectForRule(world, pos, QuasiConnectivityRules.COPPER_BULB);
    }

}
