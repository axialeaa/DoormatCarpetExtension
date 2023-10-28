package com.axialeaa.doormat.mixin.rule.disableEndPortalCrossing;

import com.axialeaa.doormat.DoormatSettings;
import com.llamalad7.mixinextras.injector.ModifyExpressionValue;
import net.minecraft.block.BlockState;
import net.minecraft.block.EndPortalBlock;
import net.minecraft.entity.Entity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

@Mixin(EndPortalBlock.class)
public class EndPortalBlockMixin {

    @SuppressWarnings("unused") // otherwise it will throw an error at "state", "world", "pos" and "entity"
    @ModifyExpressionValue(method = "onEntityCollision", at = @At(value = "INVOKE", target = "Lnet/minecraft/entity/Entity;canUsePortals()Z"))
    private boolean disableTeleportation(boolean original, BlockState state, World world, BlockPos pos, Entity entity) {
        return original && !DoormatSettings.disableEndPortalCrossing;
    }

}
