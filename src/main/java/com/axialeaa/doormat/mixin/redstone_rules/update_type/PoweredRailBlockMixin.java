package com.axialeaa.doormat.mixin.redstone_rules.update_type;

import com.axialeaa.doormat.helpers.RedstoneRuleHelper;
import com.axialeaa.doormat.util.UpdateTypeRules;
import com.llamalad7.mixinextras.injector.WrapWithCondition;
import net.minecraft.block.Block;
import net.minecraft.block.PoweredRailBlock;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyArg;

@Mixin(PoweredRailBlock.class)
public class PoweredRailBlockMixin {

    @ModifyArg(method = "updateBlockState", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/World;setBlockState(Lnet/minecraft/util/math/BlockPos;Lnet/minecraft/block/BlockState;I)Z"))
    private int changeUpdateType(int flags) {
        return UpdateTypeRules.ruleValues.get(UpdateTypeRules.RAIL).getFlags();
    }

    @WrapWithCondition(method = "updateBlockState", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/World;updateNeighborsAlways(Lnet/minecraft/util/math/BlockPos;Lnet/minecraft/block/Block;)V", ordinal = 0))
    private boolean disableNeighborUpdates(World world, BlockPos pos, Block block) {
        return RedstoneRuleHelper.neighbourUpdateForRule(UpdateTypeRules.RAIL);
    }

}
