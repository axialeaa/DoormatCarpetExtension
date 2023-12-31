package com.axialeaa.doormat.mixin.redstone_rules.update_type;

import com.axialeaa.doormat.helpers.RedstoneRuleHelper;
import com.axialeaa.doormat.util.RedstoneRule;
import com.llamalad7.mixinextras.injector.WrapWithCondition;
import net.minecraft.block.Block;
import net.minecraft.block.PistonBlock;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyArg;

@Mixin(PistonBlock.class)
public class PistonBlockMixin {

    @ModifyArg(method = "onSyncedBlockEvent", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/World;setBlockState(Lnet/minecraft/util/math/BlockPos;Lnet/minecraft/block/BlockState;I)Z", ordinal = 1))
    private int changeUpdateType(int flags) {
        return RedstoneRuleHelper.getRuleFlags(RedstoneRule.PISTON) | Block.MOVED;
    }

    @WrapWithCondition(method = "onSyncedBlockEvent", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/World;updateNeighbors(Lnet/minecraft/util/math/BlockPos;Lnet/minecraft/block/Block;)V", ordinal = 0))
    private boolean disableNeighborUpdates_onSyncedBlockEvent(World world, BlockPos pos, Block block) {
        return RedstoneRuleHelper.shouldUpdateNeighbours(RedstoneRule.PISTON);
    }

    @WrapWithCondition(method = "move", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/World;updateNeighborsAlways(Lnet/minecraft/util/math/BlockPos;Lnet/minecraft/block/Block;)V"))
    private boolean disableNeighborUpdates_move(World world, BlockPos pos, Block block) {
        return RedstoneRuleHelper.shouldUpdateNeighbours(RedstoneRule.PISTON);
    }

}
