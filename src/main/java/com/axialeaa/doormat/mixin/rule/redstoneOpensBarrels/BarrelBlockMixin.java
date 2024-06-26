package com.axialeaa.doormat.mixin.rule.redstoneOpensBarrels;

import com.axialeaa.doormat.DoormatSettings;
import com.axialeaa.doormat.mixin.extensibility.AbstractBlockMixin;
import com.axialeaa.doormat.tinker_kit.TinkerKit;
import net.minecraft.block.BarrelBlock;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvents;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(BarrelBlock.class)
public class BarrelBlockMixin extends AbstractBlockMixin {

    /**
     * If the rule is enabled, flips the open blockstate of the barrel when it receives a neighbour update and the open state doesn't match whether or not it's powered.
     */
    @Override
    public void neighborUpdateImpl(BlockState state, World world, BlockPos pos, Block sourceBlock, BlockPos sourcePos, boolean notify, CallbackInfo ci) {
        if (DoormatSettings.redstoneOpensBarrels) {
            boolean bl = TinkerKit.isReceivingRedstonePower(world, pos, state);

            if (state.get(BarrelBlock.OPEN) != bl) {
                world.playSound(null, pos, bl ? SoundEvents.BLOCK_BARREL_OPEN : SoundEvents.BLOCK_BARREL_CLOSE, SoundCategory.BLOCKS);
                world.setBlockState(pos, state.with(BarrelBlock.OPEN, bl), TinkerKit.getFlags(state, Block.NOTIFY_ALL));
            }
        }
    }

}