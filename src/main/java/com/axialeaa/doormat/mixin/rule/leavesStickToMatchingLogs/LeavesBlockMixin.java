package com.axialeaa.doormat.mixin.rule.leavesStickToMatchingLogs;

import com.axialeaa.doormat.DoormatSettings;
import com.axialeaa.doormat.helper.LogLeavesHashmapHelper;
import com.llamalad7.mixinextras.injector.ModifyExpressionValue;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.LeavesBlock;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.WorldAccess;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(LeavesBlock.class)
public class LeavesBlockMixin {

    @Unique private static Block BLOCK = null;

    @Inject(method = "updateDistanceFromLogs", at = @At("HEAD"))
    private static void storeArg(BlockState state, WorldAccess world, BlockPos pos, CallbackInfoReturnable<BlockState> cir) {
        if (DoormatSettings.leavesStickToMatchingLogs)
            BLOCK = state.getBlock();
    }

    @ModifyExpressionValue(method = "getOptionalDistanceFromLog", at = @At(value = "INVOKE", target = "Lnet/minecraft/block/BlockState;contains(Lnet/minecraft/state/property/Property;)Z"))
    private static boolean checkForLeaves(boolean original, BlockState state) {
        return DoormatSettings.leavesStickToMatchingLogs ? original && BLOCK == state.getBlock() : original;
    }

    @ModifyExpressionValue(method = "getOptionalDistanceFromLog", at = @At(value = "INVOKE", target = "Lnet/minecraft/block/BlockState;isIn(Lnet/minecraft/registry/tag/TagKey;)Z"))
    private static boolean checkForLog(boolean original, BlockState state) {
        if (!DoormatSettings.leavesStickToMatchingLogs)
            return original;

        try {
            Block block = state.getBlock();
            return LogLeavesHashmapHelper.MAP.get(BLOCK).contains(block);
        }
        catch (NullPointerException e) {
            return original;
        }
    }

}
