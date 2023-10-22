package com.axialeaa.doormat.mixin.rule.disableNether;

import com.axialeaa.doormat.DoormatSettings;
import net.minecraft.block.AbstractFireBlock;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.world.World;
import net.minecraft.world.WorldAccess;
import net.minecraft.world.dimension.NetherPortal;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.Optional;

@Mixin(AbstractFireBlock.class)
public class AbstractFireBlockMixin {

    @Redirect(method = "onBlockAdded", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/dimension/NetherPortal;getNewPortal(Lnet/minecraft/world/WorldAccess;Lnet/minecraft/util/math/BlockPos;Lnet/minecraft/util/math/Direction$Axis;)Ljava/util/Optional;"))
    private Optional<NetherPortal> disablePortalCreation(WorldAccess world, BlockPos pos, Direction.Axis axis) {
        return world.getDimension().bedWorks() && DoormatSettings.disableNether ? Optional.empty() : NetherPortal.getNewPortal(world, pos, Direction.Axis.X);
    }

    @Inject(method = "shouldLightPortalAt", at = @At("RETURN"), cancellable = true)
    private static void disableLightingBehavior(World world, BlockPos pos, Direction direction, CallbackInfoReturnable<Boolean> cir) {
        if (world.getDimension().bedWorks() && DoormatSettings.disableNether)
            cir.setReturnValue(false);
    }

}
