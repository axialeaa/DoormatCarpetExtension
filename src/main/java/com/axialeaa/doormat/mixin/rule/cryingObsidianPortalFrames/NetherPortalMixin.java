package com.axialeaa.doormat.mixin.rule.cryingObsidianPortalFrames;

import com.axialeaa.doormat.DoormatSettings;
import net.minecraft.block.AbstractBlock;
import net.minecraft.block.Blocks;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.world.WorldAccess;
import net.minecraft.world.dimension.NetherPortal;
import org.spongepowered.asm.mixin.*;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(NetherPortal.class)
public class NetherPortalMixin {

    @Shadow @Final @Mutable private static AbstractBlock.ContextPredicate IS_VALID_FRAME_BLOCK;

    @Inject(method = "<init>", at = @At("TAIL"))
    private void addValidFrameBlock(WorldAccess _world, BlockPos _pos, Direction.Axis axis, CallbackInfo ci) {
        IS_VALID_FRAME_BLOCK = (state, world, pos) -> state.isOf(Blocks.OBSIDIAN) || state.isOf(Blocks.CRYING_OBSIDIAN) && DoormatSettings.cryingObsidianPortalFrames;
    }

}
