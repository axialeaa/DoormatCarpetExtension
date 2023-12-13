package com.axialeaa.doormat.mixin.redstone_rules.update_type;

import com.axialeaa.doormat.helpers.RedstoneRuleHelper;
import com.axialeaa.doormat.util.RedstoneRule;
import net.minecraft.block.DispenserBlock;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyArg;

@Mixin(DispenserBlock.class)
public class DispenserBlockMixin {

    @ModifyArg(method = "neighborUpdate", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/World;setBlockState(Lnet/minecraft/util/math/BlockPos;Lnet/minecraft/block/BlockState;I)Z"))
    private int changeUpdateType(int flags) {
        return RedstoneRuleHelper.getRuleFlags(RedstoneRule.DISPENSER);
    }

}
