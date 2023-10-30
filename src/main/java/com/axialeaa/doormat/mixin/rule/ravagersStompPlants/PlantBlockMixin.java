package com.axialeaa.doormat.mixin.rule.ravagersStompPlants;

import com.axialeaa.doormat.DoormatSettings;
import com.axialeaa.doormat.mixin.AbstractBlockMixin;
import net.minecraft.block.BlockState;
import net.minecraft.block.PlantBlock;
import net.minecraft.entity.Entity;
import net.minecraft.entity.mob.RavagerEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.GameRules;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(PlantBlock.class)
public class PlantBlockMixin extends AbstractBlockMixin {

    @Override
    public void injectedOnEntityCollision(BlockState state, World world, BlockPos pos, Entity entity, CallbackInfo ci) {
        if (DoormatSettings.ravagersStompPlants && entity instanceof RavagerEntity && world.getGameRules().getBoolean(GameRules.DO_MOB_GRIEFING))
            // if the rule and mob griefing are enabled and the entity colliding with the block is a ravager, break the block
            world.breakBlock(pos, true, entity);
    }

}
