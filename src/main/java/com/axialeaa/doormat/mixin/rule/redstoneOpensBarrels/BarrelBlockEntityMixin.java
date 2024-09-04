package com.axialeaa.doormat.mixin.rule.redstoneOpensBarrels;

import com.axialeaa.doormat.helper.BarrelItemDumpingHelper;
import com.axialeaa.doormat.mixin.impl.BlockEntityImplMixin;
import com.axialeaa.doormat.setting.DoormatSettings;
import com.axialeaa.doormat.tinker_kit.TinkerKit;
import com.llamalad7.mixinextras.injector.v2.WrapWithCondition;
import com.llamalad7.mixinextras.sugar.Local;
import net.minecraft.block.BarrelBlock;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.entity.BarrelBlockEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvent;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(BarrelBlockEntity.class)
public abstract class BarrelBlockEntityMixin extends BlockEntityImplMixin {

    /**
     * Stops players from being able to change the open state of the barrel while it's activated by redstone.
     */
    @WrapWithCondition(method = "setOpen", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/World;setBlockState(Lnet/minecraft/util/math/BlockPos;Lnet/minecraft/block/BlockState;I)Z"))
    private boolean shouldChangeState(World world, BlockPos pos, BlockState state, int flags) {
        Block block = state.getBlock();
        return !DoormatSettings.redstoneOpensBarrels || !TinkerKit.isReceivingRedstonePower(world, pos, block);
    }

    @WrapWithCondition(method = "playSound", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/World;playSound(Lnet/minecraft/entity/player/PlayerEntity;DDDLnet/minecraft/sound/SoundEvent;Lnet/minecraft/sound/SoundCategory;FF)V"))
    private boolean shouldPlaySound(World world, PlayerEntity source, double x, double y, double z, SoundEvent sound, SoundCategory category, float volume, float pitch, @Local(argsOnly = true) BlockState state) {
        Block block = state.getBlock();
        return !DoormatSettings.redstoneOpensBarrels || !TinkerKit.isReceivingRedstonePower(world, this.getPos(), block);
    }

    @Override
    public void markDirtyImpl(CallbackInfo ci) {
        BlockState blockState = this.getCachedState();

        if (blockState.get(BarrelBlock.OPEN))
            BarrelItemDumpingHelper.dumpItemStacks(this.getWorld(), this.getPos(), blockState, BarrelBlockEntity.class.cast(this));
    }

}