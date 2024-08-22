package com.axialeaa.doormat.mixin.rule.sporeBlossomDuplication;

import com.axialeaa.doormat.DoormatSettings;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.particle.ParticleUtil;
import net.minecraft.item.BoneMealItem;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.particle.ParticleTypes;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(BoneMealItem.class)
public class BoneMealItemMixin {

    @Inject(method = "useOnFertilizable", at = @At(value = "INVOKE", target = "Lnet/minecraft/block/BlockState;getBlock()Lnet/minecraft/block/Block;", shift = At.Shift.AFTER), cancellable = true)
    private static void dropSporeBlossomOnUse(ItemStack stack, World world, BlockPos pos, CallbackInfoReturnable<Boolean> cir) {
        BlockState blockState = world.getBlockState(pos);

        if (DoormatSettings.sporeBlossomDuplication && blockState.isOf(Blocks.SPORE_BLOSSOM)) {
            Block.dropStack(world, pos, new ItemStack(Items.SPORE_BLOSSOM));
            ParticleUtil.spawnParticlesAround(world, pos, 15, ParticleTypes.HAPPY_VILLAGER);

            cir.setReturnValue(true);
        }
    }

}
