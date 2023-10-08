package com.axialeaa.doormat.mixin.rule.thornyRoseBush;

import com.axialeaa.doormat.DoormatSettings;
import com.axialeaa.doormat.entity.DoormatDamageTypes;
import com.axialeaa.doormat.mixin.AbstractBlockMixin;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.block.TallFlowerBlock;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(TallFlowerBlock.class)
public class TallFlowerBlockMixin extends AbstractBlockMixin {

    @Override
    public void injectedOnEntityCollision(BlockState state, World world, BlockPos pos, Entity entity, CallbackInfo ci) {
        if (DoormatSettings.thornyRoseBush && state.getBlock() == Blocks.ROSE_BUSH && entity instanceof LivingEntity && entity.getType() != EntityType.BEE) {
            // if thornyRoseBush is "true", the block in question is a rose bush and the entity is living and not a bee, damage will be dealt on motion
            if (!world.isClient && (entity.lastRenderX != entity.getX() || entity.lastRenderZ != entity.getZ())) {
                double velocityX = Math.abs(entity.getX() - entity.lastRenderX);
                double velocityZ = Math.abs(entity.getZ() - entity.lastRenderZ);
                if (velocityX >= 0.003 || velocityZ >= 0.003) {
                    entity.damage(world.getDamageSources().create(DoormatDamageTypes.ROSE_BUSH), 1.0F);
                }
            }
        }
    }

}
