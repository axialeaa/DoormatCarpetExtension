package com.axialeaa.doormat.mixin.rule.parityFireAspectLighting;

import com.axialeaa.doormat.DoormatSettings;
import net.minecraft.block.*;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.enchantment.Enchantments;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemUsageContext;
import net.minecraft.state.property.Properties;
import net.minecraft.util.ActionResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.event.GameEvent;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(Item.class)
public class ItemMixin {

    @Unique
    private boolean canBeLit(BlockState state) {
        return CampfireBlock.canBeLit(state) || CandleBlock.canBeLit(state) || CandleCakeBlock.canBeLit(state);
    }

    @Inject(method = "useOnBlock", at = @At("HEAD"), cancellable = true)
    public void fireAspectLight(ItemUsageContext ctx, CallbackInfoReturnable<ActionResult> cir) {
        PlayerEntity player = ctx.getPlayer();
        ItemStack stack = ctx.getStack();
        World world = ctx.getWorld();
        BlockPos pos = ctx.getBlockPos();
        BlockState state = world.getBlockState(pos);
        if (DoormatSettings.parityFireAspectLighting && EnchantmentHelper.getLevel(Enchantments.FIRE_ASPECT, stack) > 0 && (state.getBlock() instanceof TntBlock || canBeLit(state))) {
            if (state.getBlock() instanceof TntBlock) {
                TntBlock.primeTnt(world, pos);
                world.setBlockState(pos, Blocks.AIR.getDefaultState(), DoormatSettings.tntUpdateType.getFlags() | Block.REDRAW_ON_MAIN_THREAD);
            }
            else if (canBeLit(state)) {
                world.setBlockState(pos, state.with(Properties.LIT, true), Block.NOTIFY_ALL | Block.REDRAW_ON_MAIN_THREAD);
                world.emitGameEvent(player, GameEvent.BLOCK_CHANGE, pos);
            }
            stack.damage(1, (LivingEntity)player, (p) -> p.sendToolBreakStatus(ctx.getHand()));
            cir.setReturnValue(ActionResult.success(world.isClient()));
        }
    }

}
