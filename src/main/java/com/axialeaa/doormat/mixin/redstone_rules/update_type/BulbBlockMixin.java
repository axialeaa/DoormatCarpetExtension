package com.axialeaa.doormat.mixin.redstone_rules.update_type;

import com.axialeaa.doormat.util.UpdateTypeRules;
import net.minecraft.block.BulbBlock;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyArg;

@Mixin(BulbBlock.class)
public class BulbBlockMixin {

    @ModifyArg(method = "update", at = @At(value = "INVOKE", target = "Lnet/minecraft/server/world/ServerWorld;setBlockState(Lnet/minecraft/util/math/BlockPos;Lnet/minecraft/block/BlockState;I)Z"))
    private int changeUpdateType_neighborUpdate(int flags) {
        return UpdateTypeRules.ruleValues.get(UpdateTypeRules.COPPER_BULB).getFlags();
    }

}
