package com.axialeaa.doormat.mixin.rule.cryingObsidianFormation;

import com.axialeaa.doormat.setting.DoormatSettings;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.block.NetherPortalBlock;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.state.property.EnumProperty;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.util.math.random.Random;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(NetherPortalBlock.class)
public class NetherPortalBlockMixin {

    @Shadow @Final public static EnumProperty<Direction.Axis> AXIS;

    @Inject(method = "randomTick", at = @At("HEAD"))
    private void createCryingObsidian(BlockState state, ServerWorld world, BlockPos pos, Random random, CallbackInfo ci) {
        if (!DoormatSettings.cryingObsidianPortalFrames || DoormatSettings.obsidianFrameConversionChance <= 0)
            return;

        for (Direction direction : Direction.values()) {
            if (!direction.getAxis().isVertical() || state.get(AXIS) != direction.getAxis())
                continue;

            BlockPos blockPos = pos.offset(direction);
            BlockState blockState = world.getBlockState(blockPos);

            if (blockState.isOf(Blocks.OBSIDIAN) && random.nextFloat() < DoormatSettings.obsidianFrameConversionChance)
                world.setBlockState(blockPos, Blocks.CRYING_OBSIDIAN.getDefaultState());
        }
    }

}
