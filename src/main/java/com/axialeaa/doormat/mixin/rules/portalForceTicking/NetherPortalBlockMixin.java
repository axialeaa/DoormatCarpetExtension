package com.axialeaa.doormat.mixin.rules.portalForceTicking;

import com.axialeaa.doormat.DoormatSettings;
import com.axialeaa.doormat.mixin.extensibility.AbstractBlockMixin;
import net.minecraft.block.BlockState;
import net.minecraft.block.NetherPortalBlock;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.random.Random;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(NetherPortalBlock.class)
public class NetherPortalBlockMixin extends AbstractBlockMixin {

    /**
     * Immediately randomTicks the block when it's added into the world, as long as the rule is enabled.
     */
    @Override
    public void onBlockAddedImpl(BlockState state, World world, BlockPos pos, BlockState oldState, boolean notify, CallbackInfo ci) {
        Random random = world.getRandom();
        if (DoormatSettings.portalForceTicking)
            state.randomTick((ServerWorld)world, pos, random);
    }

}
