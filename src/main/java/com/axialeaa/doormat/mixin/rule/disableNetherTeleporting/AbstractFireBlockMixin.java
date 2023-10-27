package com.axialeaa.doormat.mixin.rule.disableNetherTeleporting;

import com.axialeaa.doormat.DoormatSettings;
import net.minecraft.block.AbstractFireBlock;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.world.WorldAccess;
import net.minecraft.world.dimension.NetherPortal;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

import java.util.Optional;

@Mixin(AbstractFireBlock.class)
public class AbstractFireBlockMixin {

//    @Redirect(method = "onBlockAdded", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/dimension/NetherPortal;getNewPortal(Lnet/minecraft/world/WorldAccess;Lnet/minecraft/util/math/BlockPos;Lnet/minecraft/util/math/Direction$Axis;)Ljava/util/Optional;"))
//    private Optional<NetherPortal> disablePortalCreation(WorldAccess world, BlockPos pos, Direction.Axis axis) {
//        return world.getDimension().bedWorks() && DoormatSettings.disableNetherTeleporting ? Optional.empty() : NetherPortal.getNewPortal(world, pos, axis);
//    }

    @Redirect(method = "shouldLightPortalAt", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/dimension/NetherPortal;getNewPortal(Lnet/minecraft/world/WorldAccess;Lnet/minecraft/util/math/BlockPos;Lnet/minecraft/util/math/Direction$Axis;)Ljava/util/Optional;"))
    private static Optional<NetherPortal> disableLightingBehavior(WorldAccess world, BlockPos pos, Direction.Axis axis) {
        return world.getDimension().bedWorks() && DoormatSettings.disableNetherTeleporting ? Optional.empty() : NetherPortal.getNewPortal(world, pos, axis);
    }

}
