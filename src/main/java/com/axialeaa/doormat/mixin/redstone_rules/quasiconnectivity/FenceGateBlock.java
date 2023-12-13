package com.axialeaa.doormat.mixin.redstone_rules.quasiconnectivity;

import com.axialeaa.doormat.helpers.RedstoneRuleHelper;
import com.axialeaa.doormat.util.RedstoneRule;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

@Mixin(net.minecraft.block.FenceGateBlock.class)
public class FenceGateBlock {

    @Redirect(method = "neighborUpdate", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/World;isReceivingRedstonePower(Lnet/minecraft/util/math/BlockPos;)Z"))
    private boolean allowQuasiConnecting(World world, BlockPos pos) {
        return RedstoneRuleHelper.quasiConnectForRule(world, pos, RedstoneRule.FENCE_GATE);
    }

}
