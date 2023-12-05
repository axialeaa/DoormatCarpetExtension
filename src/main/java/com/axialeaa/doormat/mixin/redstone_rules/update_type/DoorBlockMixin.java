package com.axialeaa.doormat.mixin.redstone_rules.update_type;

import com.axialeaa.doormat.util.UpdateTypeRules;
import net.minecraft.block.Block;
import net.minecraft.block.DoorBlock;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyArg;

@Mixin(DoorBlock.class)
public class DoorBlockMixin {

    @ModifyArg(method = "onUse", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/World;setBlockState(Lnet/minecraft/util/math/BlockPos;Lnet/minecraft/block/BlockState;I)Z"))
    private int changeUpdateType_onUse(int flags) {
        return UpdateTypeRules.ruleValues.get(UpdateTypeRules.DOOR).getFlags() | Block.REDRAW_ON_MAIN_THREAD;
    }

    @ModifyArg(method = "neighborUpdate", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/World;setBlockState(Lnet/minecraft/util/math/BlockPos;Lnet/minecraft/block/BlockState;I)Z"))
    private int changeUpdateType_neighborUpdate(int flags) {
        return UpdateTypeRules.ruleValues.get(UpdateTypeRules.DOOR).getFlags();
    }

    @ModifyArg(method = "setOpen", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/World;setBlockState(Lnet/minecraft/util/math/BlockPos;Lnet/minecraft/block/BlockState;I)Z"))
    private int changeUpdateType_setOpen(int flags) {
        return UpdateTypeRules.ruleValues.get(UpdateTypeRules.DOOR).getFlags() | Block.REDRAW_ON_MAIN_THREAD;
    }

}
