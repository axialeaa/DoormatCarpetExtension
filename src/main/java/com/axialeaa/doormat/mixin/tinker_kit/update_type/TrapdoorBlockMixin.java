package com.axialeaa.doormat.mixin.tinker_kit.update_type;

import com.axialeaa.doormat.tinker_kit.TinkerKit;
import net.minecraft.block.BlockState;
import net.minecraft.block.TrapdoorBlock;
import net.minecraft.util.math.BlockPos;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyArg;

@Mixin(TrapdoorBlock.class)
public class TrapdoorBlockMixin {

    @ModifyArg(method = "flip", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/World;setBlockState(Lnet/minecraft/util/math/BlockPos;Lnet/minecraft/block/BlockState;I)Z"))
    private int changeUpdateType(BlockPos pos, BlockState state, int flags) {
        return TinkerKit.getFlags(state, flags);
    }

}
