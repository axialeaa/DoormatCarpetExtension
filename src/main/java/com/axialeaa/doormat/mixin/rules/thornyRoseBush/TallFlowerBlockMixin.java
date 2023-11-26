package com.axialeaa.doormat.mixin.rules.thornyRoseBush;

import com.axialeaa.doormat.DoormatSettings;
import com.axialeaa.doormat.entity.DoormatDamageTypes;
import com.axialeaa.doormat.mixin.extensibility.AbstractBlockMixin;
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

import java.util.Date;

@Mixin(TallFlowerBlock.class)
public class TallFlowerBlockMixin extends AbstractBlockMixin {

    /**
     * For as long as the rule is enabled, the block is a rose bush and the entity is not a bee, damage it when it has a velocity of 0.003 or more in x or z.
     */
    @Override
    public void onEntityCollisionImpl(BlockState state, World world, BlockPos pos, Entity entity, CallbackInfo ci) {
        if (DoormatSettings.thornyRoseBush && state.getBlock() == Blocks.ROSE_BUSH && entity instanceof LivingEntity && entity.getType() != EntityType.BEE)
            if (!world.isClient && (entity.lastRenderX != entity.getX() || entity.lastRenderZ != entity.getZ())) {
                double velocityX = Math.abs(entity.getX() - entity.lastRenderX);
                double velocityZ = Math.abs(entity.getZ() - entity.lastRenderZ);
                if (velocityX >= 0.003 || velocityZ >= 0.003)
                    entity.damage(world.getDamageSources().create(DoormatDamageTypes.ROSE_BUSH), 1.0F);
            }
    }

}
