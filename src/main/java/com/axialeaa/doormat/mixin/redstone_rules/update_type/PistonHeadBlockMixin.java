package com.axialeaa.doormat.mixin.redstone_rules.update_type;

import com.axialeaa.doormat.helpers.RedstoneRuleHelper;
import com.axialeaa.doormat.util.RedstoneRule;
import com.llamalad7.mixinextras.injector.ModifyExpressionValue;
import net.minecraft.block.PistonHeadBlock;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

@Mixin(PistonHeadBlock.class)
public class PistonHeadBlockMixin {

    @ModifyExpressionValue(method = "neighborUpdate", at = @At(value = "INVOKE", target = "Lnet/minecraft/block/BlockState;canPlaceAt(Lnet/minecraft/world/WorldView;Lnet/minecraft/util/math/BlockPos;)Z"))
    private boolean disableNeighborUpdates(boolean original) {
        return original && RedstoneRuleHelper.shouldUpdateNeighbours(RedstoneRule.PISTON);
    }

}
