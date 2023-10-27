package com.axialeaa.doormat.mixin.rule.disableNetherTeleporting;

import com.axialeaa.doormat.DoormatSettings;
import com.llamalad7.mixinextras.injector.WrapWithCondition;
import net.minecraft.block.NetherPortalBlock;
import net.minecraft.entity.Entity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

@SuppressWarnings("unused")
@Mixin(NetherPortalBlock.class)
public class NetherPortalBlockMixin {

    @WrapWithCondition(method = "onEntityCollision", at = @At(value = "INVOKE", target = "Lnet/minecraft/entity/Entity;setInNetherPortal(Lnet/minecraft/util/math/BlockPos;)V"))
    private boolean disableTeleportation(Entity entity, BlockPos pos) {
        World world = entity.getWorld();
        return !(world.getDimension().bedWorks() && DoormatSettings.disableNetherTeleporting);
    }

}
