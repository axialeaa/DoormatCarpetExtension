package com.axialeaa.doormat.mixin.redstone_rules.update_type;

import com.axialeaa.doormat.helpers.RedstoneRuleHelper;
import com.axialeaa.doormat.util.RedstoneRule;
import net.minecraft.block.CrafterBlock;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyArg;

@Mixin(CrafterBlock.class)
public class CrafterBlockMixin {

    @ModifyArg(method = "neighborUpdate", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/World;setBlockState(Lnet/minecraft/util/math/BlockPos;Lnet/minecraft/block/BlockState;I)Z"))
    private int changeUpdateType_neighborUpdate(int flags) {
        return RedstoneRuleHelper.getRuleFlags(RedstoneRule.CRAFTER);
    }

}
