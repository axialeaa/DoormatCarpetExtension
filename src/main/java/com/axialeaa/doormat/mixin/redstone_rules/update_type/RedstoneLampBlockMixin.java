package com.axialeaa.doormat.mixin.redstone_rules.update_type;

import com.axialeaa.doormat.util.UpdateTypeRules;
import net.minecraft.block.RedstoneLampBlock;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyArg;

@Mixin(RedstoneLampBlock.class)
public class RedstoneLampBlockMixin {

    @ModifyArg(method = "neighborUpdate", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/World;setBlockState(Lnet/minecraft/util/math/BlockPos;Lnet/minecraft/block/BlockState;I)Z"))
    private int changeUpdateType_neighborUpdate(int flags) {
        return UpdateTypeRules.ruleValues.get(UpdateTypeRules.REDSTONE_LAMP).getFlags();
    }

    @ModifyArg(method = "scheduledTick", at = @At(value = "INVOKE", target = "Lnet/minecraft/server/world/ServerWorld;setBlockState(Lnet/minecraft/util/math/BlockPos;Lnet/minecraft/block/BlockState;I)Z"))
    private int changeUpdateType_scheduledTick(int flags) {
        return UpdateTypeRules.ruleValues.get(UpdateTypeRules.REDSTONE_LAMP).getFlags();
    }
    // we need 2 separate ones here between one is ServerWorld.setBlockState and the other is World.setBlockState (cringe)

}