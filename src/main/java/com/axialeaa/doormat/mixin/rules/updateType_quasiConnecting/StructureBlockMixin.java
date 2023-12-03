package com.axialeaa.doormat.mixin.rules.updateType_quasiConnecting;

import com.axialeaa.doormat.DoormatSettings;
import com.axialeaa.doormat.helpers.RedstoneHelper;
import net.minecraft.block.StructureBlock;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

@Mixin(StructureBlock.class)
public class StructureBlockMixin {

    @Redirect(method = "neighborUpdate", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/World;isReceivingRedstonePower(Lnet/minecraft/util/math/BlockPos;)Z"))
    private boolean allowQuasiConnecting(World world, BlockPos pos) {
        return RedstoneHelper.quasiConnectForRule(world, pos, DoormatSettings.structureBlockQuasiConnecting);
    }

}
