package com.axialeaa.doormat.mixin.rules.thornyRoseBush;

import com.axialeaa.doormat.DoormatSettings;
import com.axialeaa.doormat.entity.DoormatDamageTypes;
import com.axialeaa.doormat.mixin.extendables.AbstractBlockMixin;
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
        if (DoormatSettings.thornyRoseBush && state.getBlock() == Blocks.ROSE_BUSH && entity instanceof LivingEntity && entity.getType() != EntityType.BEE)
            // if the rules is enabled, the block in question is a rose bush and the entity is living and not a bee...
            if (!world.isClient && (entity.lastRenderX != entity.getX() || entity.lastRenderZ != entity.getZ())) { // if this method is called on the server side and the entity is moving...
                double velocityX = Math.abs(entity.getX() - entity.lastRenderX); // get the absolute velocity of the entity in both x and z
                double velocityZ = Math.abs(entity.getZ() - entity.lastRenderZ);
                if (velocityX >= 0.003 || velocityZ >= 0.003) // if the velocity is greater than or equal to 0.003 in either x or z...
                    entity.damage(world.getDamageSources().create(DoormatDamageTypes.ROSE_BUSH), 1.0F); // deal rose bush damage
            }
    }

}
