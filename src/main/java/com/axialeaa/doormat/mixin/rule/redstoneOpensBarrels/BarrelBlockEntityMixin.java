package com.axialeaa.doormat.mixin.rule.redstoneOpensBarrels;

import com.axialeaa.doormat.DoormatSettings;
import com.axialeaa.doormat.tinker_kit.TinkerKit;
import com.llamalad7.mixinextras.injector.v2.WrapWithCondition;
import net.minecraft.block.BlockState;
import net.minecraft.block.entity.BarrelBlockEntity;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.block.entity.BlockEntityType;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvent;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

@Mixin(BarrelBlockEntity.class)
public class BarrelBlockEntityMixin extends BlockEntity {

    public BarrelBlockEntityMixin(BlockEntityType<?> type, BlockPos pos, BlockState state) {
        super(type, pos, state);
    }

    /**
     * Stops players from being able to change the open state of the barrel while it's activated by redstone.
     */
    @WrapWithCondition(method = "setOpen", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/World;setBlockState(Lnet/minecraft/util/math/BlockPos;Lnet/minecraft/block/BlockState;I)Z"))
    private boolean shouldChangeState(World world, BlockPos pos, BlockState state, int flags) {
        return !(DoormatSettings.redstoneOpensBarrels && TinkerKit.isReceivingPowerWithinRange(world, pos));
    }

    @WrapWithCondition(method = "playSound", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/World;playSound(Lnet/minecraft/entity/player/PlayerEntity;DDDLnet/minecraft/sound/SoundEvent;Lnet/minecraft/sound/SoundCategory;FF)V"))
    private boolean shouldPlaySound(World world, PlayerEntity source, double x, double y, double z, SoundEvent sound, SoundCategory category, float volume, float pitch) {
        return !(DoormatSettings.redstoneOpensBarrels && TinkerKit.isReceivingPowerWithinRange(world, pos));
    }

}