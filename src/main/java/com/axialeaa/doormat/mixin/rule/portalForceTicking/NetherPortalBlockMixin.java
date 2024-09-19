package com.axialeaa.doormat.mixin.rule.portalForceTicking;

import com.axialeaa.doormat.mixin.impl.AbstractBlockImplMixin;
import com.axialeaa.doormat.setting.DoormatSettings;
import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import net.minecraft.block.BlockState;
import net.minecraft.block.NetherPortalBlock;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;

@Mixin(NetherPortalBlock.class)
public class NetherPortalBlockMixin extends AbstractBlockImplMixin {

    @Override
    public void onBlockAddedImpl(BlockState state, World world, BlockPos pos, BlockState oldState, boolean notify, Operation<Void> original) {
        if (DoormatSettings.portalForceTicking && world instanceof ServerWorld serverWorld)
            state.randomTick(serverWorld, pos, world.getRandom());

        original.call(state, world, pos, oldState, notify);
    }

}