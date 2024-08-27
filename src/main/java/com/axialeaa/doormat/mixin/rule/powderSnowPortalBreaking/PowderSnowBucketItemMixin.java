package com.axialeaa.doormat.mixin.rule.powderSnowPortalBreaking;

import com.axialeaa.doormat.settings.DoormatSettings;
import com.llamalad7.mixinextras.injector.ModifyExpressionValue;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.PowderSnowBucketItem;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

@Mixin(PowderSnowBucketItem.class)
public class PowderSnowBucketItemMixin {

    @ModifyExpressionValue(method = "placeFluid", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/World;isAir(Lnet/minecraft/util/math/BlockPos;)Z"))
    private boolean shouldReplaceBlockAtPos(boolean original, @Nullable PlayerEntity player, World world, BlockPos pos, @Nullable BlockHitResult hitResult) {
        BlockState blockState = world.getBlockState(pos);
        return original || DoormatSettings.powderSnowPortalBreaking && blockState.isOf(Blocks.NETHER_PORTAL);
    }

}
