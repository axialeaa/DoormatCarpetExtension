package com.axialeaa.doormat.mixin.rule.ravagersStomp;

import com.axialeaa.doormat.mixin.impl.AbstractBlockImplMixin;
import com.axialeaa.doormat.setting.DoormatSettings;
import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import net.minecraft.block.BlockState;
import net.minecraft.block.SnowBlock;
import net.minecraft.entity.Entity;
import net.minecraft.entity.mob.RavagerEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.GameRules;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;

@Mixin(SnowBlock.class)
public class SnowBlockMixin extends AbstractBlockImplMixin {

    @Override
    public void onEntityCollisionImpl(BlockState state, World world, BlockPos pos, Entity entity, Operation<Void> original) {
        if (DoormatSettings.ravagersStompSnow && entity instanceof RavagerEntity && world.getGameRules().getBoolean(GameRules.DO_MOB_GRIEFING))
            world.breakBlock(pos, true, entity);

        original.call(state, world, pos, entity);
    }

}
