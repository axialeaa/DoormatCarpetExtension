package com.axialeaa.doormat.mixin.rule.ravagersStomp;

import com.axialeaa.doormat.settings.DoormatSettings;
import com.axialeaa.doormat.mixin.impl.AbstractBlockImplMixin;
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
public class PlantBlockMixin extends AbstractBlockImplMixin {

    /**
     * Breaks the block at the entity's position if the rule is enabled, mob griefing is on and the entity is a ravager.
     */
    @Override
    public void onEntityCollisionImpl(BlockState state, World world, BlockPos pos, Entity entity, CallbackInfo ci) {
        if (DoormatSettings.ravagersStompPlants && entity instanceof RavagerEntity && world.getGameRules().getBoolean(GameRules.DO_MOB_GRIEFING))
            world.breakBlock(pos, true, entity);
    }

}