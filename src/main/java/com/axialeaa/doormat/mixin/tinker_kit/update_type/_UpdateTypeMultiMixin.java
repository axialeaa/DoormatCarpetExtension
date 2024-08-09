package com.axialeaa.doormat.mixin.tinker_kit.update_type;

import com.axialeaa.doormat.tinker_kit.TinkerKit;
import com.llamalad7.mixinextras.sugar.Local;
import net.minecraft.block.AbstractSkullBlock;
import net.minecraft.block.BellBlock;
import net.minecraft.block.BlockState;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyArg;

@Mixin({
    AbstractSkullBlock.class,
    BellBlock.class
})
public class _UpdateTypeMultiMixin {

    @ModifyArg(method = "neighborUpdate", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/World;setBlockState(Lnet/minecraft/util/math/BlockPos;Lnet/minecraft/block/BlockState;I)Z"))
    private int changeUpdateType(int original, @Local(argsOnly = true) BlockState state) {
        return TinkerKit.getFlags(state, original);
    }

}
