package com.axialeaa.doormat.mixin.redstone_rules.update_type;

import com.axialeaa.doormat.util.UpdateTypeRules;
import net.minecraft.block.BellBlock;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyArg;

@Mixin(BellBlock.class)
public class BellBlockMixin {

    @ModifyArg(method = "neighborUpdate", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/World;setBlockState(Lnet/minecraft/util/math/BlockPos;Lnet/minecraft/block/BlockState;I)Z"))
    private int changeUpdateType(int flags) {
        return UpdateTypeRules.ruleValues.get(UpdateTypeRules.BELL).getFlags();
    }

}
