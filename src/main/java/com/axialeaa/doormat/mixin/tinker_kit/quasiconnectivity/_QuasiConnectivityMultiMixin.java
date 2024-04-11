package com.axialeaa.doormat.mixin.tinker_kit.quasiconnectivity;

import com.axialeaa.doormat.tinker_kit.TinkerKit;
import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import net.minecraft.block.*;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

@Mixin({
    AbstractSkullBlock.class,
    BellBlock.class,
    CommandBlock.class,
    CrafterBlock.class,
    FenceGateBlock.class,
    NoteBlock.class,
    RedstoneLampBlock.class,
    StructureBlock.class,
    TntBlock.class,
    TrapdoorBlock.class
})
public class _QuasiConnectivityMultiMixin {

    @WrapOperation(method = "neighborUpdate", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/World;isReceivingRedstonePower(Lnet/minecraft/util/math/BlockPos;)Z"))
    private boolean allowQuasiConnecting(World instance, BlockPos pos, Operation<Boolean> original) {
        return TinkerKit.isReceivingRedstonePower(instance, pos);
    }

}
