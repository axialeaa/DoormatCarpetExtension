package com.axialeaa.doormat.mixin.rule.zoglinsSpawnInPortals;

import com.axialeaa.doormat.setting.DoormatSettings;
import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import net.minecraft.block.BlockState;
import net.minecraft.block.NetherPortalBlock;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.mob.HostileEntity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.random.Random;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

@Mixin(NetherPortalBlock.class)
public class NetherPortalBlockMixin {

    /**
     * For as long as the rule is enabled, spawn a zoglin at a 10% chance (if the position is valid).
     */
    @WrapOperation(method = "randomTick", at = @At(value = "FIELD", target = "Lnet/minecraft/entity/EntityType;ZOMBIFIED_PIGLIN:Lnet/minecraft/entity/EntityType;"))
    private EntityType<? extends HostileEntity> modifySpawnedMob(Operation<EntityType<? extends HostileEntity>> original, BlockState state, ServerWorld world, BlockPos pos, Random random) {
        return DoormatSettings.zoglinPortalSpawnChance > 0 && random.nextFloat() < DoormatSettings.zoglinPortalSpawnChance ? EntityType.ZOGLIN : original.call();
    }

}
