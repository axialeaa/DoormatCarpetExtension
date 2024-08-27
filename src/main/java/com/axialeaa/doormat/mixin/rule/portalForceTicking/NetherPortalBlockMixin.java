package com.axialeaa.doormat.mixin.rule.portalForceTicking;

import com.axialeaa.doormat.settings.DoormatSettings;
import com.axialeaa.doormat.mixin.impl.AbstractBlockImplMixin;
import net.minecraft.block.BlockState;
import net.minecraft.block.NetherPortalBlock;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(NetherPortalBlock.class)
public class NetherPortalBlockMixin extends AbstractBlockImplMixin {

    /**
     * Immediately randomTicks the block when it's added into the world, as long as the rule is enabled.
     */
    @Override
    public void onBlockAddedImpl(BlockState state, World world, BlockPos pos, BlockState oldState, boolean notify, CallbackInfo ci) {
        if (DoormatSettings.portalForceTicking && world instanceof ServerWorld serverWorld)
            state.randomTick(serverWorld, pos, world.getRandom());
    }

}