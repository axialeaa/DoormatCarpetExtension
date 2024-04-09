package com.axialeaa.doormat.mixin.rule.campfireRespawning;

import com.axialeaa.doormat.helper.CampfireRespawningHelper;
import net.minecraft.block.BlockState;
import net.minecraft.block.CampfireBlock;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Hand;
import net.minecraft.util.ItemActionResult;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(CampfireBlock.class)
public class CampfireBlockMixin {

    @Inject(method = "onUseWithItem", at = @At(value = "INVOKE", target = "Ljava/util/Optional;isPresent()Z", shift = At.Shift.BEFORE), cancellable = true)
    private void setSpawn(ItemStack stack, BlockState state, World world, BlockPos pos, PlayerEntity player, Hand hand, BlockHitResult hit, CallbackInfoReturnable<ItemActionResult> cir) {
        cir.setReturnValue(CampfireRespawningHelper.setSpawnAt(world, pos, state, player, stack));
    }

}
