package com.axialeaa.doormat.mixin.redstone_rules.update_type;

import com.axialeaa.doormat.util.UpdateTypeRules;
import net.minecraft.block.Block;
import net.minecraft.block.Blocks;
import net.minecraft.block.TntBlock;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyArg;
import org.spongepowered.asm.mixin.injection.Redirect;

@Mixin(TntBlock.class)
public class TntBlockMixin {

    @Redirect(method = { "onBlockAdded", "neighborUpdate", "onProjectileHit" }, at = @At(value = "INVOKE", target = "Lnet/minecraft/world/World;removeBlock(Lnet/minecraft/util/math/BlockPos;Z)Z"))
    private boolean changeUpdateType(World world, BlockPos pos, boolean move) {
        return world.setBlockState(pos, Blocks.AIR.getDefaultState(), UpdateTypeRules.ruleValues.get(UpdateTypeRules.TNT).getFlags());
        // reconstructing the function of removeBlock() is necessary here, because neighbor updates are intrinsic to that method
    }

    @ModifyArg(method = "onUse", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/World;setBlockState(Lnet/minecraft/util/math/BlockPos;Lnet/minecraft/block/BlockState;I)Z"))
    private int changeUpdateType_onUse(int flags) {
        return UpdateTypeRules.ruleValues.get(UpdateTypeRules.TNT).getFlags() | Block.REDRAW_ON_MAIN_THREAD;
    }

}
