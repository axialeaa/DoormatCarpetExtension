package com.axialeaa.doormat.mixin.tinker_kit;

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
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyArg;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(value = BarrelBlockEntity.class, priority = 1500)
public class BarrelBlockEntityMixin extends BlockEntityImplMixin {

    @Unique private final BarrelBlockEntity thisBlockEntity = BarrelBlockEntity.class.cast(this);

    @ModifyArg(method = "setOpen", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/World;setBlockState(Lnet/minecraft/util/math/BlockPos;Lnet/minecraft/block/BlockState;I)Z"))
    private int modifyUpdateType(int original, @Local(argsOnly = true) BlockState state) {
        Block block = state.getBlock();
        return TinkerKit.getFlags(block, original);
    }

    @WrapWithCondition(method = "setOpen", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/World;setBlockState(Lnet/minecraft/util/math/BlockPos;Lnet/minecraft/block/BlockState;I)Z"))
    private boolean shouldChangeState(World world, BlockPos pos, BlockState state, int flags) {
        Block block = state.getBlock();
        return !DoormatSettings.redstoneOpensBarrels || !TinkerKit.isReceivingRedstonePower(world, pos, block);
    }

    @WrapWithCondition(method = "playSound", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/World;playSound(Lnet/minecraft/entity/player/PlayerEntity;DDDLnet/minecraft/sound/SoundEvent;Lnet/minecraft/sound/SoundCategory;FF)V"))
    private boolean shouldPlaySound(World world, PlayerEntity source, double x, double y, double z, SoundEvent sound, SoundCategory category, float volume, float pitch, @Local(argsOnly = true) BlockState state) {
        Block block = state.getBlock();
        return !DoormatSettings.redstoneOpensBarrels || !TinkerKit.isReceivingRedstonePower(world, thisBlockEntity.getPos(), block);
    }

    @Override
    public void markDirtyImpl(CallbackInfo ci) {
        BlockState blockState = thisBlockEntity.getCachedState();

        if (blockState.get(BarrelBlock.OPEN)) {
            World world = thisBlockEntity.getWorld();
            BlockPos blockPos = thisBlockEntity.getPos();

            BarrelItemDumpingHelper.dumpItemStacks(world, blockPos, blockState, thisBlockEntity);
        }
    }

}
